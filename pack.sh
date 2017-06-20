echo tar
rm -f htdocs/webcalc_v1.0.tar.gz
tar -cvzf htdocs/webcalc_v1.0.tar.gz *.bat start.sh src htdocs/*.html htdocs/*.htm htdocs/*.jpg htdocs/*.css htdocs/*.js htdocs/*.ico
echo zip
rm -f htdocs/webcalc_v1.0.zip
zip htdocs/webcalc_v1.0.zip *.bat start.sh src htdocs/*.html htdocs/*.htm htdocs/*.jpg htdocs/*.css htdocs/*.js htdocs/*.ico
