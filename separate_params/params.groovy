def all() {
    return [
        string(
            name: 'foobar',
            defaultValue: 'FOOBAR',
            description: 'param1',
        ),
        string(
            name: 'fizzbuzz',
            defaultValue: 'FIZZBUZZ',
            description: 'param2',
        )
    ]
}

return this
