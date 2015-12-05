# 1 Environment 
## 1.1 OS(Ubuntu 14.04 LTS)

### install

    $ make
    $ sudo make install
    
    $ which redis-server
    /usr/local/bin/redis-server
    $ which redis-cli
    /usr/local/bin/redis-cli
    
    # all admin commands
    $ redis-
    redis-benchmark   redis-check-aof   redis-check-dump  redis-cli         redis-sentinel    redis-server
    
    $ redis-server --version
    Redis server v=3.0.3 sha=00000000:0 malloc=jemalloc-3.6.0 bits=64 build=2c6cc6387d738ff8

### test

    $ redis-server
    
    $ redis-cli
    127.0.0.1:6379> ping
    PONG
    127.0.0.1:6379> set mykey somevalue
    OK
    127.0.0.1:6379> get mykey
    "somevalue"
    127.0.0.1:6379> 
    127.0.0.1:6379> help DEL

    DEL key [key ...]
    summary: Delete a key
    since: 1.0.0
    group: generic

    127.0.0.1:6379> del mykey
    (integer) 1
    127.0.0.1:6379> get mykey
    (nil)
    127.0.0.1:6379> 


## 1.2 Docker(1.9.1)

    $ sudo docker run --name some-redis -d redis

    # define port in host
    $ sudo docker run -p 6379:6379 --name redis redis

			# view logs
			$ sudo docker logs -f redis    
    
    # run new thread in container
    $ sudo docker exec -it redis /bin/bash
    
# 2 Reference

[Spring Data Redis Version 1.6.1.RELEASE, 2015-11-15](http://docs.spring.io/spring-data/redis/docs/1.6.1.RELEASE/reference/html)    


