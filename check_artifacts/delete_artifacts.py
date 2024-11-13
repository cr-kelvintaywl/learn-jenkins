"""Deletes artifacts on the Jenkins node.
"""

from argparse import ArgumentParser
import os
import shutil


def _get_job_name_from_env() -> str:
    return os.environ["JOB_NAME"]


def _get_job_build_dir(job_name: str):
    jenkins_home = os.environ["JENKINS_HOME"]
    job_name_in_path = job_name.replace("/", "/jobs/")
    job_dir = os.path.join(jenkins_home, "jobs", job_name_in_path)
    return os.path.join(job_dir, "builds")


def _list_builds(job_name: str):
    build_dir = _get_job_build_dir(job_name)
    print(build_dir)
    items = [
        os.path.join(build_dir, dir)
        for dir in os.listdir(build_dir)
    ]

    dirs = list(filter(lambda d: os.path.isdir(d) and os.path.basename(d).isdigit(), items))
    # each dir's basename is the build number (integer)
    # so we want to sort by that build number as an integer, not string
    dirs.sort(key=lambda d: int(os.path.basename(d)))
    return dirs


def delete_artifacts(job_name: str, up_until: int):
    """Delete artifacts from a job's builds up until N builds ago.
    
    In other words, we do not delete artifacts for the last N builds.
    """
    build_dirs = _list_builds(job_name)

    to_delete = build_dirs[:-up_until]
    print(f"Context: Examining from {to_delete[0]} ... {to_delete[-1]}")

    for build_dir in to_delete:
        archive_dir = os.path.join(build_dir, "archive")
        if os.path.exists(archive_dir) and os.path.isdir(archive_dir):
            shutil.rmtree(archive_dir)
            print(f"Removed {archive_dir}")


def parse_args():
    p = ArgumentParser(description=__doc__)
    p.add_argument(
        "--job_name",
        type=str,
        help="Jenkins job name. If empty, assumes from JOB_NAME env var.",
        default="",
    )
    p.add_argument(
        "-n",
        "--until",
        type=int,
        help="preserves artifacts for up to last N builds, where N is this limit. Deletes others.",
        default=10,
    )

    return p.parse_args()


if __name__ == "__main__":
    args = parse_args()
    N = args.until
    job_name = args.job_name or _get_job_name_from_env()

    print(f"Preserving latest {N} builds...")
    delete_artifacts(job_name, N)
