# Itsucks.io

Your friendly neighbourhood bashing tool

## Run in dev mode

To prepare dev environment:

- Install leiningen
- Install bower
- Install Docker stuff
```
brew install docker-machine
brew install docker-compose
```

- `docker-compose up -d`
- `bower install`

Now simply use `lein figwheel` to run in development mode, this will watch and hotreload and runs both the frontend and backend. The migrations will run auto-magically.