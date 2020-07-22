#!/usr/bin/env bash
if [ -f ./.env.local ]
then
  export $(cat .env | sed 's/#.*//g' | xargs)
fi