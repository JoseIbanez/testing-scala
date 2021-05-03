curl -X POST \
  -d '{"id":"aa","name":"jose","age":10, "countryOfResidence":"spain", "address":{"street":"s1","city":"madrid" } }' \
  -H "Content-type: Application/json" \
  http://127.0.0.1:8080/users



curl http://127.0.0.1:8080/users

curl http://127.0.0.1:8080/users/jose

for i in {1..100} ; do curl -v http://127.0.0.1:8080/users/5fc163c3-2c6c-4f7b-b3a1-542b07fb8cd6 & done
