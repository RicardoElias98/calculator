#!/bin/sh
HOSTPORT=$1
shift
CMD="$@"

HOST=$(echo $HOSTPORT | cut -d: -f1)
PORT=$(echo $HOSTPORT | cut -d: -f2)

echo "Wainting $HOST:$PORT..."
while ! nc -z $HOST $PORT; do
  sleep 1
done

echo "$HOST:$PORT available..."
exec $CMD
