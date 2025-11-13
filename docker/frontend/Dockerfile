# -----------------------------
# 1️⃣ Build Stage
# -----------------------------
FROM node:18 AS build-stage

# Set working directory inside the container
WORKDIR /app

# Copy package.json and package-lock.json for dependency installation
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the application source code
COPY . .

# Build the Angular app in production mode
RUN npm run build --prod

# -----------------------------
# 2️⃣ Production Stage (Nginx)
# -----------------------------
FROM nginx:alpine

# Copy custom nginx config (optional but recommended)
COPY nginx.conf /etc/nginx/nginx.conf

# Copy built app from build stage to Nginx web root
COPY --from=build-stage /app/dist/bound-frontend /usr/share/nginx/html

# Expose port 80 to serve the app
EXPOSE 80

# Start Nginx server
CMD ["nginx", "-g", "daemon off;"]
