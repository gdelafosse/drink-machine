Drink Machine
=============

```
mvn clean install
mvn tomee:build
mvn tomee:run
```

## Stateless implementation

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

## Stateful implementation

Put coins one by one

```
POST http://localhost:8080/api/buying/coin/fifty_cents

201 OK
50

POST http://localhost:8080/api/buying/coin/one_euro

201 OK
150
```

and get the drink you want :

```
GET http://localhost:8080/api/buying/drink/coca

200 OK
{
  "two_euros": 0,
  "one_euro": 0,
  "fifty_cents": 0,
  "twenty_cents": 1,
  "ten_cents": 1,
  "five_cents": 0,
  "two_cents": 0,
  "one_cent": 0,
  "total": 30
}
```

## GraphQL implementation

GraphIQL available on http://localhost:8080/
GraphQL api available on http://localhost:8080/graphql

```
{
  drinks(pagination:{
    limit:10,
    offset:0
  }, sort: [
    {
      property: "amount",
      direction: DESC
    }, {
      property: "price",
      direction: ASC
    }, {
      property: "name",
      direction: DESC
    }
  ],
  criterias: [
    {
      operator: EQ,
      property: "name",
      argument: "orangina"
    }
  ]){
    values {
      name
      amount
      price
    }
    result{totalCount,count,offset}
  }
}

200 OK
{
  "data": {
    "drinks": {
      "values": [
        {
          "name": "orangina",
          "amount": 20,
          "price": 120
        }
      ],
      "result": {
        "totalCount": 5,
        "count": 1,
        "offset": 0
      }
    }
  }
}
```