linkCheckRes=$(java -jar MDLinkCheckerCLI.jar "$@")
echo "[MDLinkCheck]: $linkCheckRes"
if [[ $linkCheckRes == *"ERROR"* ]]
then
  exit 1
fi
