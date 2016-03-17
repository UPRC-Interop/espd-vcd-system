#-*- mode: ruby -*-
# vi: set ft=ruby :
Vagrant.configure(2) do |config|
  # config.vm.box_check_update = false

  config.vm.network "private_network", ip: "192.168.33.10"

  # config.vm.network "public_network"

  config.vm.provider "virtualbox" do |vb|
    vb.name = "espd-vcd-dev"
    vb.memory = 4096
    vb.cpus = 1
  end

  config.vm.box = "bento/centos-6.7"
  config.vm.network "forwarded_port", guest: 26000, host: 26000 # for tomcat 6
  config.vm.network "forwarded_port", guest: 27000, host: 27000 # for tomcat 7
  config.vm.network "forwarded_port", guest: 28000, host: 28000 # for tomcat 8
  config.vm.network "forwarded_port", guest: 29000, host: 29000 # for tomcat 9
  config.vm.network "forwarded_port", guest: 5432, host: 65432 # for postgresql 9.4

  # sync directories
  config.vm.synced_folder "./artifacts", "/tmp/artifacts", create: true, owner: 501, group: 501, create: true, mount_options: ["dmode=777,fmode=666"]

  # shell provisioning
  config.vm.provision "shell", inline: <<-SHELL
    sudo yum -y install https://yum.puppetlabs.com/puppetlabs-release-el-6.noarch.rpm
    yum -y install puppet
    puppet module install puppetlabs-java
    puppet module install puppetlabs-tomcat
    puppet module install puppetlabs-postgresql
  SHELL

  # puppet provisioning
  config.vm.provision "puppet" do |puppet|
    puppet.manifests_path = "puppet/manifests"
    puppet.module_path = "puppet/modules"
    puppet.manifest_file = "site.pp"
  end
end