#!/usr/bin/env bash
if [ -f ./.env.local ]
then
  export $(cat ./.env.local | sed 's/#.*//g' | xargs)
fi