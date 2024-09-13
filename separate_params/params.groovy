def foobar = string(
    name: 'foobar',
    defaultValue: 'FOOBAR',
    description: 'param1',
)

def fizzbuzz = string(
    name: 'fizzbuzz',
    defaultValue: 'FIZZBUZZ',
    description: 'param2',
)

return [
    foobar,
    fizzbuzz,
]
