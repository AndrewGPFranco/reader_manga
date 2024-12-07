FROM node:18-alpine

WORKDIR /app

RUN apk update && apk add --no-cache git

COPY . .

EXPOSE 3000

CMD ["npm", "run", "dev"]