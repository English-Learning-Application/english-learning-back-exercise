aws ecr get-login-password --region ap-southeast-2 | docker login --username AWS --password-stdin 761018889743.dkr.ecr.ap-southeast-2.amazonaws.com
docker build -t exercise-microservice .
docker tag exercise-microservice:latest 761018889743.dkr.ecr.ap-southeast-2.amazonaws.com/exercise-microservice:latest
docker push 761018889743.dkr.ecr.ap-southeast-2.amazonaws.com/exercise-microservice:latest
kubectl delete deployment exercise-service-deployment
kubectl apply -f deployment.yaml