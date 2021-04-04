#!/bin/bash

curl -v -X POST \
    -d '{"street":"Calle","city":"Madrid","country":"ES"}' \
    -H "Content-type: application/json" \
    http://localhost:8080/addre22


curl -v -X POST \
    -d @./src/test/sampleData/AppDirectSubscriptionOrderSample1.json \
    -H "Content-type: application/json" \
    http://localhost:8080/order


