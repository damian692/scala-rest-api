# scala-rest-api  
  
Before building app make sure you have installed java 11  

  sudo apt install openjdk-11-jre-headless  
  
and sbt  

  sudo apt-get update  
  sudo apt-get install apt-transport-https curl gnupg -yqq  
  echo "deb https://repo.scala-sbt.org/scalasbt/debian all main" | sudo tee /etc/apt/sources.list.d/sbt.list  
  echo "deb https://repo.scala-sbt.org/scalasbt/debian /" | sudo tee /etc/apt/sources.list.d/sbt_old.list  
  curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x2EE0EA64E40A89B84B2DF73499E82A75642AC823" | sudo -H gpg --no-default-keyring --keyring gnupg-ring:/etc/apt/trusted.gpg.d/scalasbt-release.gpg --import  
  sudo chmod 644 /etc/apt/trusted.gpg.d/scalasbt-release.gpg  
  sudo apt-get update  
  sudo apt-get install sbt  
  
Then you can run build_run.sh or run.sh script  
  
usecase.sh contains requests and shows responses   
bad_requests.sh contains requests that will fail due to validation requirements  
