# 모든 서비스 도커 이미지를 빌드합니다.
services=(
  "glowgrow-eureka" "glowgrow-gateway" "glowgrow-auth" "glowgrow-user"
  "glowgrow-payment" "glowgrow-notification" "glowgrow-post" "glowgrow-promotion" "glowgrow-reservation"
)

# 도커 이미지에 commit hash를 기반으로한 이미지 태그를 설정합니다.
commit_hash=$(git rev-parse --short HEAD)

for service in "${services[@]}"
do
  imageName="$DOCKER_HUB_NAMESPACE/$service"

  # 도커 이미지 빌드 (해당 service 디렉토리에 Dockerfile이 있어야 합니다.)
  docker build -t "$imageName:latest" "./$service"

  # 이미지를 구분하기 위해서 latest 이외의 태그를 추가합니다.
  docker tag "$imageName:latest" "$imageName:$commit_hash"

  # Docker Hub에 push
  docker push "$imageName:latest"
  docker push "$imageName:$commit_hash"

  echo "$service 이미지가 빌드되어 Docker hub에 푸쉬되었습니다."
done

echo "모든 서비스의 이미지 빌드 및 푸쉬가 완료되었습니다."
