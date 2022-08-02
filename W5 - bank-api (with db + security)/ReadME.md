# Banking API

#### Create Account

```http
POST /v1/accounts
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `name` | `string` | **Required**. Account holder name |
| `surname` | `string` | **Required**. Account holder surname |
| `email` | `string` | **Required**. Account holder email |
| `tc` | `string` | **Required**. Account holder tc |
| `type` | `string` | **Required**. Account type |

#### Get Account Detail

```http
GET /v1/accounts/{accountNumber}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `accountNumber`| `string` | **Required**. Account number |

#### Deposit 

```http
PATCH /v1/accounts/{accountNumber}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `accountNumber`| `string` | **Required**. Account number |
| `balance`| `double` | **Required**. Balance |


#### Transfer Money

```http
PATCH /v1/accounts/transfer/{accountNumber}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `accountNumber`| `string` | **Required**. Account number |
| `transferredAccountNumber`| `string` | **Required**. Account to transfer money |
| `amount`| `double` | **Required**. Money to be transferred |

#### Get Account Detail

```http
GET /v1/accounts/logs/{accountNumber}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `accountNumber`| `string` | **Required**. Account number to show related logs |


#
- [CollectAPI](https://collectapi.com/api/economy/gold-currency-and-exchange-api)
#



