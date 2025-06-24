#!/bin/bash

# Enhanced Docker Deployment Script
echo "=== Enhanced Docker Deployment ==="

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if .env file exists
check_env_file() {
    if [ ! -f .env ]; then
        print_warning ".env file not found. Creating default .env file..."
        cat > .env << EOF
JWT_SECRET=Y6ThdM9XcJb35ZBYzLH7HhT7dwFDAJ0dUVZn7pa9JIE=
MAIL_USERNAME=saifromdhani707@gmail.com
MAIL_PASSWORD=dfswhascenogfexh
SPRING_MAIL_HOST=smtp.gmail.com
SPRING_MAIL_PORT=587
APP_MAIL_FROM=saifromdhani707@gmail.com
APP_FRONTEND_URL=http://localhost:4200
APP_CORS_ALLOWED_ORIGINS=http://localhost:4200
MYSQL_ROOT_PASSWORD=
MYSQL_DATABASE=UserPI
DOCKER_REGISTRY=your-registry
EOF
        print_status ".env file created successfully"
    fi
}

# Pre-deployment checks
pre_deployment_checks() {
    print_status "Running pre-deployment checks..."

    # Check if Docker is running
    if ! docker info > /dev/null 2>&1; then
        print_error "Docker is not running. Please start Docker and try again."
        exit 1
    fi

    # Check if docker-compose is available
    if ! command -v docker-compose &> /dev/null; then
        print_error "docker-compose is not installed. Please install docker-compose."
        exit 1
    fi

    # Check if required directories exist
    for dir in "services/config-server" "services/discovery" "services/gateway" "services/User"; do
        if [ ! -d "$dir" ]; then
            print_error "Directory $dir not found!"
            exit 1
        fi
    done

    print_status "Pre-deployment checks passed!"
}

# Build and run with Docker Compose
docker_deploy() {
    print_status "Building and deploying with Docker Compose..."

    # Clean up any existing containers
    print_status "Cleaning up existing containers..."
    docker-compose down -v 2>/dev/null || true

    # Remove dangling images
    docker image prune -f

    # Build all images with no cache to ensure fresh builds
    print_status "Building Docker images..."
    docker-compose build --no-cache --parallel

    if [ $? -ne 0 ]; then
        print_error "Docker build failed!"
        exit 1
    fi

    # Start services
    print_status "Starting services..."
    docker-compose up -d

    if [ $? -ne 0 ]; then
        print_error "Failed to start services!"
        exit 1
    fi

    # Wait for services to be healthy
    print_status "Waiting for services to be healthy..."
    sleep 30

    # Check status
    docker-compose ps

    # Show logs for failed services
    failed_services=$(docker-compose ps --services --filter "status=exited")
    if [ ! -z "$failed_services" ]; then
        print_warning "Some services failed to start. Showing logs:"
        for service in $failed_services; do
            print_error "Logs for $service:"
            docker-compose logs $service
        done
    fi

    print_status "Application deployed! Access points:"
    echo "- Config Server: http://localhost:8888"
    echo "- Discovery Service: http://localhost:8761"
    echo "- Gateway Service: http://localhost:8222"
    echo "- User Service: http://localhost:8090"
    echo "- User Service API Docs: http://localhost:8090/api/swagger-ui.html"
}

# Kubernetes Deployment Script
k8s_deploy() {
    print_status "=== Kubernetes Deployment ==="

    # Check if kubectl is available
    if ! command -v kubectl &> /dev/null; then
        print_error "kubectl is not installed. Please install kubectl."
        exit 1
    fi

    # Create namespace
    print_status "Creating namespace..."
    kubectl apply -f k8s/01-namespace.yaml

    # Apply ConfigMap and Secrets
    print_status "Applying ConfigMap and Secrets..."
    kubectl apply -f k8s/02-configmap.yaml
    kubectl apply -f k8s/03-secrets.yaml

    # Deploy MySQL
    print_status "Deploying MySQL..."
    kubectl apply -f k8s/04-mysql.yaml

    # Wait for MySQL to be ready
    print_status "Waiting for MySQL to be ready..."
    kubectl wait --for=condition=available --timeout=300s deployment/mysql -n microservices

    # Deploy Config Server
    print_status "Deploying Config Server..."
    kubectl apply -f k8s/05-config-server.yaml

    # Wait for Config Server
    print_status "Waiting for Config Server to be ready..."
    kubectl wait --for=condition=available --timeout=300s deployment/config-server -n microservices

    # Deploy Discovery Service
    print_status "Deploying Discovery Service..."
    kubectl apply -f k8s/06-discovery-service.yaml

    # Wait for Discovery Service
    print_status "Waiting for Discovery Service to be ready..."
    kubectl wait --for=condition=available --timeout=300s deployment/discovery-service -n microservices

    # Deploy User Service
    print_status "Deploying User Service..."
    kubectl apply -f k8s/07-user-service.yaml

    # Wait for User Service
    print_status "Waiting for User Service to be ready..."
    kubectl wait --for=condition=available --timeout=300s deployment/user-service -n microservices

    # Deploy Gateway Service
    print_status "Deploying Gateway Service..."
    kubectl apply -f k8s/08-gateway-service.yaml

    # Wait for Gateway Service
    print_status "Waiting for Gateway Service to be ready..."
    kubectl wait --for=condition=available --timeout=300s deployment/gateway-service -n microservices

    # Show status
    kubectl get all -n microservices

    print_status "Kubernetes deployment completed!"
    print_status "Access the application through the Gateway Service LoadBalancer IP"
}

# Build Docker images for Kubernetes
build_images() {
    print_status "Building Docker images for Kubernetes..."

    # Load registry from .env
    source .env
    REGISTRY=${DOCKER_REGISTRY:-"your-registry"}

    # Build each service
    print_status "Building config-server..."
    docker build -t $REGISTRY/config-server:latest ./services/config-server

    print_status "Building discovery-service..."
    docker build -t $REGISTRY/discovery-service:latest ./services/discovery

    print_status "Building user-service..."
    docker build -t $REGISTRY/user-service:latest ./services/User

    print_status "Building gateway-service..."
    docker build -t $REGISTRY/gateway-service:latest ./services/gateway

    print_status "All images built successfully!"

    # Push to registry (uncomment if using remote registry)
    # print_status "Pushing images to registry..."
    # docker push $REGISTRY/config-server:latest
    # docker push $REGISTRY/discovery-service:latest
    # docker push $REGISTRY/user-service:latest
    # docker push $REGISTRY/gateway-service:latest
}

# Cleanup functions
docker_cleanup() {
    print_status "Cleaning up Docker deployment..."
    docker-compose down -v
    docker system prune -f
    print_status "Docker cleanup completed!"
}

k8s_cleanup() {
    print_status "Cleaning up Kubernetes deployment..."
    kubectl delete namespace microservices
    print_status "Kubernetes cleanup completed!"
}

# Health check function
health_check() {
    print_status "Performing health checks..."

    services=("config-server:8888" "discovery-service:8761" "user-service:8090" "gateway-service:8222")

    for service in "${services[@]}"; do
        name=$(echo $service | cut -d: -f1)
        port=$(echo $service | cut -d: -f2)

        if curl -f -s http://localhost:$port/actuator/health > /dev/null 2>&1; then
            print_status "$name is healthy"
        else
            print_error "$name is not responding"
        fi
    done
}

# Main menu
case "$1" in
    "docker")
        check_env_file
        pre_deployment_checks
        docker_deploy
        ;;
    "k8s")
        check_env_file
        k8s_deploy
        ;;
    "build")
        check_env_file
        build_images
        ;;
    "docker-cleanup")
        docker_cleanup
        ;;
    "k8s-cleanup")
        k8s_cleanup
        ;;
    "health")
        health_check
        ;;
    *)
        echo "Usage: $0 {docker|k8s|build|docker-cleanup|k8s-cleanup|health}"
        echo ""
        echo "Commands:"
        echo "  docker         - Deploy using Docker Compose"
        echo "  k8s           - Deploy to Kubernetes"
        echo "  build         - Build Docker images"
        echo "  docker-cleanup - Clean up Docker deployment"
        echo "  k8s-cleanup   - Clean up Kubernetes deployment"
        echo "  health        - Check service health"
        exit 1
        ;;
esac