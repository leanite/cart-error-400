#! /bin/bash

declare port=8080
declare server=http://localhost
declare targetUrl=https://www.google.com.br/
declare folder=api
declare WIREMOCK_VERSION=2.26.3

function main() {
    case $1 in
        start)
            start ${@:2}
        ;;
        proxy)
            proxyMain ${@:2}
        ;;
        api)
            apiMain ${@:2}
        ;;
        request)
            requestMain ${@:2}
        ;;
        set)
            setMain ${@:2}
        ;;
        samples)
            samples
        ;;
        *)
            help
        ;;
    esac
}

function proxyMain() {
    case $1 in
        record)
            proxyRecord ${@:2}
        ;;
        playback)
            proxyPlayback ${@:2}
        ;;
        redirect)
            proxyRedirect ${@:2}
        ;;
        *)
            help
        ;;
    esac
}

function apiMain() {
    case $1 in
        shutdown)
            apiShutdown
        ;;
        record-start)
            apiRecordStart ${@:2}
        ;;
        record-stop)
            apiRecordStop ${@:2}
        ;;
        field)
            apiFieldChange ${@:2}
        ;;
        *)
            help
        ;;
    esac
}

function requestMain() {
    case $1 in
        wiremock)
            requestWiremock $2 ${@:3}
        ;;
        remote)
            requestRemote $2 ${@:3}
        ;;
        *)
            help
        ;;
    esac
}

function setMain() {
    for i in "$@" ; do
        case "$i" in
            --port=*|-p=*)
                port="${i#*=}"
            ;;
            --server=*|-s=*)
                server="${i#*=}"
            ;;
            --targetUrl=*)
                targetUrl="${i#*=}"
            ;;
            *) echo "Settings \"${i}\"not found"
        esac
        shift
    done
}

function samples() {
    cat <<TEXT
Wiremock Helper Samples

 Samples commands to use wiremock helper.

A simple wrapper for "java -jar wiremock.jar", so we should pass the normal wiremock commands:
 $0 start --verbose --port $port --root-dir=$folder --proxy-all=${targetUrl} --match-headers="op,pre-login" --record-mappings

Start wiremock as a proxy to record requests:
 $0 proxy record

Start wiremock with recorded requests:
 $0 proxy playback

Make a request to wiremock server using the opkey and body:
 $0 request wiremock "IclJTs4pwhNbjgrPsfXoR04K7UsidpdaVuqbfGgInmo=;"

Make a request to target server using the opkey and body:
 $0 request target "IclJTs4pwhNbjgrPsfXoR04K7UsidpdaVuqbfGgInmo=;"

Remove "securityData" field from all requests:
 $0 api field securityData disable

TEXT
}

function help() {
    cat <<TEXT
Wiremock Helper

 A tool to wrap some wiremock api commands.

Usage:

 $0 start [wiremock params]         Start wiremock without configuration. Use default wiremock parameters
 $0 proxy [proxy commands]          See "Proxy Commands" bellow
 $0 api [api commands]              See "Api Commands" bellow
 $0 request [request commands]      See "Request Commands" bellow
 $0 samples                         Show samples for commands
 $0 help                            Show this help

Proxy Commands:                     -- Reads mapped requests and:
 record                             Start server, non-mapped request will be redirected and recorded
 playback                           Start server, non-mapped requests will return 404
 redirect                           Start server, non-mapped requests will be redirected to real server

Api Commands:                       -- These commands should be called with server running
 record-start                       Send start command, target = your real server
 record-stop                        Send stop command
 field <name> enable                Send command to enabled field. This will enabled field again.

Request Commands:
 wiremock <opKey> <body>            Make a request to wiremock using the opKey and body
 target <opKey> <body>              Make a request to target using the opKey and body

Field names:
 sdkData.versionControl             App Version control, disables the "outdated" message
 securityData                       Enable/disable the "App Token"


TEXT
}

function start() {
    echo "Root dir ===> $folder"

    java -jar wiremock-jre8-standalone-${WIREMOCK_VERSION}.jar \
      $@ 
}

function proxyRecord() {
    start --verbose \
    --port $port \
    --root-dir=$folder \
    --proxy-all=${targetUrl} \
    --record-mappings
}

function proxyPlayback() {
    start --verbose \
    --port $port \
    --root-dir=$folder 
}

function proxyRedirect() {
  start --verbose \
  --port $port \
  --root-dir=$folder \
  --proxy-all=${targetUrl}
}

function apiShutdown() {
    curl -X POST ${server}:${port}/__admin/shutdown
}

function apiRecordStart() {
    curl -d '{"targetBaseurl": "$targetUrl"}' -H "Content-Type: application/json" -X POST ${server}:${port}/__admin/recordings/start
}

function apiRecordStop() {
    curl -X POST ${server}:${port}/__admin/recordings/stop
}

function apiFieldChange() {
    method=""
    case $2 in
        enable|e)
            method=PUT
        ;;
        disable|d)
            method=DELETE
        ;;
        *)
            method=GET
        ;;
    esac
    curl -X $method $server:${port}/__admin/remover/$1
}

function requestWiremock() {
    request "${server}:${port}" $1 ${@:2}
}

function requestRemote() {
    request "${targetUrl}" $1 ${@:2}
}

function request() {
    command="curl -X POST $1 -H \"op: $2\""
    if [ ! -z "$3" ]; then
        command+=" -d $3"
    fi
    if [ "log"=="true" ]; then
        echo $command
    fi
    eval $command
}

main $@
