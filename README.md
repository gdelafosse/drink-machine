Drink Machine
=============

```
mvn clean install
mvn tomee:build
mvn tomee:run
```

## REST

Checking how many coins they are in the machine :

```
GET http://localhost:8080/api/coins

200 OK
{
  "two_euros": 10,
  "one_euro": 5,
  "fifty_cents": 0,
  "twenty_cents": 0,
  "ten_cents": 0,
  "five_cents": 5,
  "two_cents": 5,
  "one_cent": 5,
  "total": 2540
}
```

Checking how many drinks they are in the machine :

```
GET http://localhost:8080/api/drinks

200 OK
[
  {
    "amount": 19,
    "price": 120,
    "name": "coca"
  },
  {
    "amount": 20,
    "price": 90,
    "name": "evian"
  },
  {
    "amount": 20,
    "price": 110,
    "name": "nestea"
  },
  {
    "amount": 20,
    "price": 120,
    "name": "orangina"
  },
  {
    "amount": 20,
    "price": 100,
    "name": "perrier"
  }
]
```

Buying a drink

```
POST http://localhost:8080/api/buy/coca
{
    "fifty_cents": 5
}

200 OK
{
  "two_euros": 0,
  "one_euro": 1,
  "fifty_cents": 0,
  "twenty_cents": 0,
  "ten_cents": 0,
  "five_cents": 5,
  "two_cents": 2,
  "one_cent": 1,
  "total": 130
}
```