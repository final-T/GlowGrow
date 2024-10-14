# 모든 서비스 도커 이미지를 빌드합니다.
services=(
"glowgrow-eureka" "glowgrow-gateway" "glowgrow-auth" "glowgrow-user glowgrow-payment"
"glowgrow-notification" "glowgrow-post" "glowgrow-promotion" "glowgrow-reservation"
)

# 도커 이미지에 commit hash를 기반으로한 이미지 태그를 설정합니다.
commit_hash=$(git rev-parse --short HEAD)

for service in "${services[@]}"
do
  imageName="$DOCKER_HUB_NAMESPACE/$service"
  # 이미지를 구분하기 위해서 latest 이외의 태그를 추가합니다.
  docker tag "$imageName:latest" "$imageName:$commit_hash"

  # docker hub 에 push
  docker push "$imageName:latest"
  docker push "$imageName:$commit_hash"

  echo "$service image is built and pushed to Docker hub"
done

echo "Build and Push processing is done"