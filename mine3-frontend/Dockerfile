FROM node:20-alpine as deps

# Define build arguments for environment variables
ARG VITE_PROJECT_ID
ARG VITE_API_BASE_URL

# Set environment variables during the build process
ENV VITE_PROJECT_ID=$VITE_PROJECT_ID
ENV VITE_API_BASE_URL=$VITE_API_BASE_URL

WORKDIR /app

RUN apk add g++ make py3-pip

COPY package.json pnpm-lock.yaml* ./
RUN corepack enable pnpm && pnpm i --frozen-lockfile

COPY . ./

FROM deps as builder
WORKDIR /app

COPY --from=deps /app/node_modules ./node_modules
COPY . .

RUN corepack enable pnpm && pnpm run build

FROM nginx:alpine as prod

COPY --from=builder /app/dist /usr/share/nginx/html
COPY ./default.conf /etc/nginx/conf.d/default.conf

CMD ["nginx", "-g", "daemon off;"]
