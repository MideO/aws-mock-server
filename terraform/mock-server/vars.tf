variable "ebs_app_name" {
  default = "mock-server"
}

variable "ebs_env_name" {
  default = "mockserver"
}

variable "ebs_instance_type" {
  default = "m4.xlarge"
}

variable "ebs_solution_stack_name" {
  default = "64bit Amazon Linux 2017.09 v2.6.0 running Java 8"
}

variable "aws_profile" {
  description = "AWS profile containing authentication credentials"
  default     = "default"
}

variable "iam_instance_profile_name" {
  description = "IAM Instance Profile NAme"
  default     = "aws-elasticbeanstalk-ec2-role"
}

variable "digital_hosted_zone" {
  default = ""
}
