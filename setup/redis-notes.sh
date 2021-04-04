#!/bin/bash

# Get all keys
redis-cli KEYS "*"

# Get a value
redis-cli GET "OrderDetails:opcoCustomerID:145"

redis-cli KEYS "Regis*"
redis-cli FLUSHALL
