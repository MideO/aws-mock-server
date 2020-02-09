provider "aws" {
  region = "eu-west-2"
  shared_credentials_file = "~/.aws/credentials"
}

resource "aws_instance" "mockserver" {
  ami = "ami-14ecf070"
  instance_type = "${var.ebs_instance_type}"

  tags {
    Name = "${var.ebs_env_name}"
    "elasticbeanstalk:environment-name" = "${var.ebs_env_name}"
    "aws:cloudformation:logical-id" = "AWSEBAutoScalingGroup"
  }
}

resource "aws_elastic_beanstalk_application" "mock-server" {
  name = "${var.ebs_app_name}"
  description = "A mock server"
}

resource "aws_elastic_beanstalk_environment" "mock-server" {
  name = "${var.ebs_env_name}"
  description = "A mock server"
  application = "${var.ebs_app_name}"
  solution_stack_name = "${var.ebs_solution_stack_name}"
  tier = "WebServer"

  setting {
    name = "InstanceType"
    namespace = "aws:autoscaling:launchconfiguration"
    value = "${var.ebs_instance_type}"
  }

  setting {
    namespace = "aws:autoscaling:launchconfiguration"
    name = "IamInstanceProfile"
    value = "aws-elasticbeanstalk-ec2-role"
  }

  setting {
    namespace = "aws:elb:listener:443"
    name = "ListenerProtocol"
    value = "HTTPS"
  }

  setting {
    namespace = "aws:elb:listener:443"
    name = "InstancePort"
    value = "80"
  }

  setting {
    namespace = "aws:elb:listener:443"
    name = "SSLCertificateId"
    value = ""
  }
}

resource "aws_route53_record" "mockserver" {
  zone_id = "${var.digital_hosted_zone}"
  name = "mockserver"
  type = "CNAME"
  ttl = "60"
  records = [
    "${data.aws_elb.lb.dns_name}"
  ]
}

data "aws_elb" "lb" {
  name = "${aws_elastic_beanstalk_environment.mock-server.load_balancers[0]}"
}
