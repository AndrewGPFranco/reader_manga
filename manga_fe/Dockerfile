FROM node:18-alpine

RUN apk update && apk add --no-cache bash git

WORKDIR /app

COPY . .

EXPOSE 3000

CMD ["npm", "run", "dev"]