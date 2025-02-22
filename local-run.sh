docker build -t exercise-microservice .
minikube image load exercise-microservice:latest
kubectl delete secret exercise-service-secret
kubectl create secret generic exercise-service-secret --from-env-file=local.env
kubectl delete deployment exercise-service-deployment
kubectl apply -f local-deployment.yaml