cheminFramework='/home/hoby/Bureau/Frameworktest/Framework/'
cheminFrameworktest='/home/hoby/Bureau/Frameworktest/'
cheminTest='/home/hoby/Bureau/Test/'
echo $cheminFramework'hoby'
#PARTIE 1
javac -d . $cheminFramework'Url.java'
javac -d . $cheminFramework'Parametre.java'
javac -d . $cheminFramework'Mapping.java'
javac -d . $cheminFramework'ModelView.java'
javac -d . $cheminFramework'UrlInconue.java'
javac -d . $cheminFramework'FrontServlet.java'
cp -r $cheminFrameworktest'annotation' $cheminFramework'classes/'
cp -r $cheminFrameworktest'etu1978' $cheminFrameworktest'classes/'
cp -r $cheminFrameworktest'exception' $cheminFrameworktest'classes/'
jar cvf $cheminTest'WEB-INF/lib/Framework.jar' -C $cheminFramework'classes' .
#PARTIE 2
mkdir Temp
mkdir -p Temp/WEB-INF Temp/WEB-INF/classes Temp/WEB-INF/lib
cp $cheminTest'WEB-INF/web.xml' $cheminFrameworktest'Temp/WEB-INF/'
cp $cheminTest*.jsp $cheminFrameworktest'Temp/'
cp $cheminTest'WEB-INF/lib/Framework.jar' $cheminFrameworktest'Temp/WEB-INF/lib/'
javac -d $cheminFrameworktest'Temp/WEB-INF/classes/' $cheminTest'src/java/test/Dept.java'
javac -d $cheminFrameworktest'Temp/WEB-INF/classes/' $cheminTest'src/java/test/Emp.java'
cd Temp
jar -cvf $cheminFrameworktest'Temp/Temp.war' *
cd ../
cp $cheminFrameworktest'Temp/Temp.war' /var/lib/tomcat9/webapps
rm -R Temp
