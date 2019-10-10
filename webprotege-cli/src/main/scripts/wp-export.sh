#!/usr/bin/env bash
# Arg1: The id of the project to export
projectIdToExport=$1
# Arg2: The WebProtege data directory
dataDirectory=$2
# Arg3: Extra args for mongoexport (see mongoexport docs)
extraArgs=$3

# Currently hard coded to output in the working directory.  Needs to be an arg.
outputDirectory=./webprotege-export
projectOutputDirectory=$outputDirectory/export-of-$projectIdToExport

# Exports a MongoDB collection, filtering by a key.  The collection is exported to a file in the working directory named with the collection name
# Args 1) Collection name, 
#      2) Key name, 
#      3) key value
function exportCollection {
	local filterQuery='{"'$2'":"'$projectIdToExport'"}' 
	mongoexport --db=webprotege --collection=$1 --query=$filterQuery --out $projectOutputDirectory/db/$1.json --quiet --pretty $extraArgs
	echo "Exported $1"

}

echo "Exporting Project $projectIdToExport"
echo "WebProtege data directory: $dataDirectory"
echo "Output directory: $outputDirectory"

# Export mongodb collections

exportCollection ProjectDetails "_id"
exportCollection EntityCrudKitSettings "_id"
exportCollection EntityDiscussionThreads "projectId"
exportCollection EntityTags "projectId"
exportCollection ProjectAccess "projectId"
exportCollection RoleAssignments "projectId"
exportCollection SlackWebhooks "projectId"
exportCollection Tags "projectId"
exportCollection Watches "projectId"

# Export project directory

webProtegeProjectDirectory=$dataDirectory/data-store/project-data/$projectIdToExport

cp -r $webProtegeProjectDirectory $projectOutputDirectory
