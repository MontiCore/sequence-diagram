# Delete "[[TOC]]" in all MD files before mirroring  
for file in $(find . -type f -name "*.md")
do 
  sed -i 's/\[\[_TOC_\]\]//' $file
done
echo "[INFO] Removed all occureneces of '[[_TOC_]]' in *.md files"

