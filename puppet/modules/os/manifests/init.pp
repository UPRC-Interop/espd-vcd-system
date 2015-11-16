#
# Author: Panagiotis NICOLAOU
# Email: pnikola@unipi.gr
#
class os {
  group { 'espdvcd':
    ensure => 'present',
    gid    => 501,
  }
  user { 'espdvcd':
    ensure            => 'present',
    gid               => 501,
    home              => '/home/espdvcd',
    managehome        => true,
    password          => '$6$OlzBC7H7rx0sMn4K$RZAz28yCQp6OlIdQV7hpHoshhUJRs4IcuvcmMoHyi0n7emV1PGH34yli0LjMHlZWovmE3noZVMCzxUd6qLEcg1',
    password_max_age  => 99999,
    password_min_age  => 0,
    shell             => '/bin/bash',
    uid               => 501,
  }
  package { 'mlocate':
    ensure => installed
  }
  package { 'zip':
    ensure => installed
  }
  package { 'unzip':
    ensure => installed
  }
}
