node default {
  class { 'os': }

  # configure tomcat
  $catalina_home = '/home/espdvcd/tomcat'

  $tomcat6_flag = 'ON'
  $tomcat7_flag = 'ON'
  $tomcat8_flag = 'ON'
  $tomcat9_flag = 'ON'

  # tomcat 6
  $tomcat6 = 'tomcat6'
  $serverPort6 = 16005
  $port6 = 26000
  $redirectPort6 = 16443
  $portAJP6 = 16009
  $url6 = 'http://apache.otenet.gr/dist/tomcat/tomcat-6/v6.0.44/bin/apache-tomcat-6.0.44.tar.gz'

  # tomcat 7
  $tomcat7 = 'tomcat7'
  $serverPort7 = 17005
  $port7 = 27000
  $redirectPort7 = 17443
  $portAJP7 = 17009
  $url7 = 'http://apache.otenet.gr/dist/tomcat/tomcat-7/v7.0.67/bin/apache-tomcat-7.0.67.tar.gz'

  # tomcat 8
  $tomcat8 = 'tomcat8'
  $serverPort8 = 18005
  $port8 = 28000
  $redirectPort8 = 18443
  $portAJP8 = 18009
  $url8 = 'http://apache.otenet.gr/dist/tomcat/tomcat-8/v8.0.30/bin/apache-tomcat-8.0.30.tar.gz'

  # tomcat 9
  $tomcat9 = 'tomcat9'
  $serverPort9 = 19005
  $port9 = 29000
  $redirectPort9 = 19443
  $portAJP9 = 19009
  $url9 = 'http://apache.forthnet.gr/tomcat/tomcat-9/v9.0.0.M1/bin/apache-tomcat-9.0.0.M1.tar.gz'

  class { 'tomcat':
    catalina_home => $catalina_home,
    group         => 'espdvcd',
    manage_group  => false,
    user          => 'espdvcd',
    manage_user   => false,
  }
  class { 'java':
    distribution => 'jdk',
    version      => 'latest',
  }

  if($tomcat6_flag == 'ON') {
    tomcat::instance { $tomcat6:
      source_url    => $url6,
      catalina_base => "$catalina_home/$tomcat6",
    }->
    tomcat::config::server { $tomcat6:
      catalina_base => "$catalina_home/$tomcat6",
      port          => $serverPort6,
    }->
    tomcat::config::server::connector { "$tomcat6-http":
      catalina_base         => "$catalina_home/$tomcat6",
      port                  => $port6,
      protocol              => 'HTTP/1.1',
      additional_attributes => {
        'redirectPort' => $redirectPort6,
      },
    }->
    tomcat::config::server::connector { "$tomcat6-ajp":
      catalina_base         => "$catalina_home/$tomcat6",
      port                  => $portAJP6,
      protocol              => 'AJP/1.3',
      additional_attributes => {
        'redirectPort' => $redirectPort6,
      },
    }->
    tomcat::service { $tomcat6:
      catalina_base => "$catalina_home/$tomcat6",
    }

    # symlink for auto-deployment of designer.war file
    file { "/home/espdvcd/tomcat/$tomcat6/webapps/designer.war":
      ensure  => 'link',
      target  => '/tmp/artifacts/designer.war',
      require => Class['tomcat'],
    }

    # symlink for auto-deployment of viewer.war file
    file { "/home/espdvcd/tomcat/$tomcat6/webapps/viewer.war":
      ensure  => 'link',
      target  => '/tmp/artifacts/viewer.war',
      require => Class['tomcat'],
    }
  }

  if($tomcat7_flag == 'ON') {
    tomcat::instance { $tomcat7:
      source_url    => $url7,
      catalina_base => "$catalina_home/$tomcat7",
    }->
    tomcat::config::server { $tomcat7:
      catalina_base => "$catalina_home/$tomcat7",
      port          => $serverPort7,
    }->
    tomcat::config::server::connector { "$tomcat7-http":
      catalina_base         => "$catalina_home/$tomcat7",
      port                  => $port7,
      protocol              => 'HTTP/1.1',
      additional_attributes => {
        'redirectPort' => $redirectPort7,
      },
    }->
    tomcat::config::server::connector { "$tomcat7-ajp":
      catalina_base         => "$catalina_home/$tomcat7",
      port                  => $portAJP7,
      protocol              => 'AJP/1.3',
      additional_attributes => {
        'redirectPort' => $redirectPort7,
      },
    }->
    tomcat::service { $tomcat7:
      catalina_base => "$catalina_home/$tomcat7",
    }

    # symlink for auto-deployment of designer.war file
    file { "/home/espdvcd/tomcat/$tomcat7/webapps/designer.war":
      ensure  => 'link',
      target  => '/tmp/artifacts/designer.war',
      require => Class['tomcat'],
    }

    # symlink for auto-deployment of viewer.war file
    file { "/home/espdvcd/tomcat/$tomcat7/webapps/viewer.war":
      ensure  => 'link',
      target  => '/tmp/artifacts/viewer.war',
      require => Class['tomcat'],
    }
  }

  if($tomcat8_flag == 'ON') {
    tomcat::instance { $tomcat8:
      source_url    => $url8,
      catalina_base => "$catalina_home/$tomcat8",
    }->
    tomcat::config::server { $tomcat8:
      catalina_base => "$catalina_home/$tomcat8",
      port          => $serverPort8,
    }->
    tomcat::config::server::connector { "$tomcat8-http":
      catalina_base         => "$catalina_home/$tomcat8",
      port                  => $port8,
      protocol              => 'HTTP/1.1',
      additional_attributes => {
        'redirectPort' => $redirectPort8,
      },
    }->
    tomcat::config::server::connector { "$tomcat8-ajp":
      catalina_base         => "$catalina_home/$tomcat8",
      port                  => $portAJP8,
      protocol              => 'AJP/1.3',
      additional_attributes => {
        'redirectPort' => $redirectPort8,
      },
    }->
    tomcat::service { 'default':
      catalina_base => "$catalina_home/$tomcat8",
    }

    # symlink for auto-deployment of designer.war file
    file { "/home/espdvcd/tomcat/$tomcat8/webapps/designer.war":
      ensure  => 'link',
      target  => '/tmp/artifacts/designer.war',
      require => Class['tomcat'],
    }

    # symlink for auto-deployment of viewer.war file
    file { "/home/espdvcd/tomcat/$tomcat8/webapps/viewer.war":
      ensure  => 'link',
      target  => '/tmp/artifacts/viewer.war',
      require => Class['tomcat'],
    }
  }

  if($tomcat9_flag == 'ON') {
    tomcat::instance { $tomcat9:
      source_url    => $url9,
      catalina_base => "$catalina_home/$tomcat9",
    }->
    tomcat::config::server { $tomcat9:
      catalina_base => "$catalina_home/$tomcat9",
      port          => $serverPort9,
    }->
    tomcat::config::server::connector { "$tomcat9-http":
      catalina_base         => "$catalina_home/$tomcat9",
      port                  => $port9,
      protocol              => 'HTTP/1.1',
      additional_attributes => {
        'redirectPort' => $redirectPort9,
      },
    }->
    tomcat::config::server::connector { "$tomcat9-ajp":
      catalina_base         => "$catalina_home/$tomcat9",
      port                  => $portAJP9,
      protocol              => 'AJP/1.3',
      additional_attributes => {
        'redirectPort' => $redirectPort9,
      },
    }->
    tomcat::service { $tomcat9:
      catalina_base => "$catalina_home/$tomcat9",
    }

    # symlink for auto-deployment of designer.war file
    file { "/home/espdvcd/tomcat/$tomcat9/webapps/designer.war":
      ensure  => 'link',
      target  => '/tmp/artifacts/designer.war',
      require => Class['tomcat'],
    }

    # symlink for auto-deployment of viewer.war file
    file { "/home/espdvcd/tomcat/$tomcat9/webapps/viewer.war":
      ensure  => 'link',
      target  => '/tmp/artifacts/viewer.war',
      require => Class['tomcat'],
    }
  }

  # JDBC URL to connect to espdvcd schema of postgres database:
  # jdbc:postgresql://localhost:65432/postgres?currentSchema=espdvcd

  # define postgresql version, locale, and encoding
  class { 'postgresql::globals':
    manage_package_repo => true,
    version             => '9.4',
    encoding            => 'UTF-8',
    locale              => 'en_US.UTF-8',
  }->
  class { 'postgresql::server':
    ip_mask_deny_postgres_user => '0.0.0.0/32',
    ip_mask_allow_all_users    => '0.0.0.0/0',
    listen_addresses           => '*',
    #    ipv4acls                   => ['hostssl all johndoe 192.168.0.0/24 cert'],
    postgres_password          =>  postgresql_password('postgres', 'postgres'),
  }

  # create database espdvcddb
  postgresql::server::db { 'espdvcddb':
    user     => 'espdvcd',
    password => postgresql_password('espdvcd', 'espdvcd'),
  }

  # manage users, roles, and permissions
  #  postgresql::server::role { 'marmot':
  #    password_hash => postgresql_password('marmot', 'mypasswd'),
  #  }
  #
  #  postgresql::server::database_grant { 'espdvcddb':
  #    privilege => 'ALL',
  #    db        => 'espdvcddb',
  #    role      => 'marmot',
  #  }
  #
  #  postgresql::server::table_grant { 'my_table of espdvcddb':
  #    privilege => 'ALL',
  #    table     => 'my_table',
  #    db        => 'espdvcddb',
  #    role      => 'marmot',
  #  }
}