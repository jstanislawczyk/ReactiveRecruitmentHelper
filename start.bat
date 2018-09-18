docker stop reactive-recruitment-helper
docker rm reactive-recruitment-helper

docker build -f Dockerfile -t reactive-recruitment-helper .
docker run --name reactive-recruitment-helper -p 8090:8090 reactive-recruitment-helper