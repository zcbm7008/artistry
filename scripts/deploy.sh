#!/usr/bin/env bash

cd /home/ec2-user/artistry

DOCKER_APP_NAME=artistry

EXIST_BLUE=$(sudo docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml ps | grep Up)

CURRENT_TIME=$(date +"%Y-%m-%d %H:%M:%S")

echo "배포 시작 : ${CURRENT_TIME}" >> /home/ec2-user/deploy.log

deploy_service() {
  local service_name=$1
  local compose_file=$2

  echo "${service_name} 시작 : ${CURRENT_TIME}" >> /home/ec2-user/deploy.log
  sudo docker-compose -p ${DOCKER_APP_NAME}-${service_name} -f ${compose_file} up -d --build
  sleep 30
}

shutdown_service() {
  local service_name=$1
  local compose_file=$2

  echo "${service_name} 중단 시작 : ${CURRENT_TIME}" >> /home/ec2-user/deploy.log
  sudo docker-compose -p ${DOCKER_APP_NAME}-${service_name} -f ${compose_file} down
  sudo docker image prune -af
  echo "${service_name} 중단 완료 : ${CURRENT_TIME}" >> /home/ec2-user/deploy.log
}

if [ -z "$EXIST_BLUE" ]; then
  deploy_service "blue" "docker-compose.blue.yml"
  shutdown_service "green" "docker-compose.green.yml"
else
  deploy_service "green" "docker-compose.green.yml"
  shutdown_service "blue" "docker-compose.blue.yml"
fi

CURRENT_TIME=$(date +"%Y-%m-%d %H:%M:%S")
echo "배포 종료 : ${CURRENT_TIME}" >> /home/ec2-user/deploy.log