# Delete "[[_TOC_]]" in all MD files before mirroring  
for file in $(find . -type f -name "*.md")
do 
  sed -i 's/\[\[_TOC_\]\]//' $file
done
echo "[INFO] Removed all occureneces of '[[_TOC_]]' in *.md files"
# Replace descriptions of not publicly reachable RWTH GitLab links
curl --location --header "PRIVATE-TOKEN: jU7hPx74kzmMGFjfGmHC" "https://git.rwth-aachen.de/api/v4/projects/monticore%2Fmdlinkchecker/jobs/artifacts/master/raw/target/libs/MDLinkChangerCLI.jar?job=build" --output MDLinkChangerCLI.jar
for file in $(find . -type f -name "*.md")
do
  content=$(java -jar MDLinkChangerCLI.jar -f $file)
  echo $content > $file
done
