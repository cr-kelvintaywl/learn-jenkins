"""Deletes artifacts on the current Jenkins node for a specific Jenkins job.

This script assumes it will be run on the Jenkins node where your job's artifacts are stored.
It will delete all builds' artifacts up to the most recent N builds.
You can specify N via the -n or --until option flag.
"""

from argparse import ArgumentParser
import os
import shutil
from typing import List


def _get_job_name_from_env() -> str:
    # Jenkins injects the job name in the JOB_NAME env var.
    return os.environ["JOB_NAME"]


def _get_job_build_dir(job_name: str):
    # NOTE: when a job name is foo/bar/fizz,
    # Jenkins store this jobs' build directory under the path: foo/jobs/bar/jobs/fizz
    job_name_in_path = job_name.replace("/", "/jobs/")

    job_dir_on_jenkins = os.path.join(
        os.environ["JENKINS_HOME"],
        "jobs",
        job_name_in_path,
    )
    return os.path.join(job_dir_on_jenkins, "builds")


def _list_builds(job_name: str) -> List:
    build_dir = _get_job_build_dir(job_name)
    items = [
        # full path
        os.path.join(build_dir, dir)
        for dir in os.listdir(build_dir)
    ]

    dirs = list(
        filter(
            # we only care about directories with digits as names
            lambda d: os.path.isdir(d) and os.path.basename(d).isdigit(),
            items
        )
    )
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
    assert N > 0, "Please specify a positive value (min: 1) for `until`"

    job_name = args.job_name or _get_job_name_from_env()

    print(f"Deleting artifacts for all builds up to latest {N} builds...")
    delete_artifacts(job_name, up_until=N)
