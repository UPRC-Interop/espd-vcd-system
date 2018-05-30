FROM gradle:4.7-alpine

WORKDIR /designer

COPY . /designer 

CMD ["gradle",":designer:designer-backend:run"]
