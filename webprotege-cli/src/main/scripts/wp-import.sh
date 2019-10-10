#!/usr/bin/env bash
# Arg1: The id of the project to import
projectIdToImport=$1
# Arg2: The WebProtege data directory
dataDirectory=$2
# Arg3: Extra args for mongoimport (see mongoimport docs)
extraArgs=$3

# Currently hard coded - should be an arg
inputDirectory=./webprotege-export
projectImportDirectory=$inputDirectory/export-of-$projectIdToImport

# Imports a MongoDB collection
# Args 1) Collection name
function importCollection {
	mongoimport --db=webprotege --collection=$1 --file $projectImportDirectory/db/$1.json $extraArgs
	echo "Imported $1"
}

echo "WebProtege Import"
echo "Importing Project $projectIdToImport"
echo "WebProtege data directory $dataDirectory"

importCollection ProjectDetails
importCollection EntityCrudKitSettings
importCollection EntityDiscussionThreads
importCollection EntityTags
importCollection ProjectAccess
importCollection RoleAssignments
importCollection SlackWebhooks
importCollection Tags
importCollection Watches
