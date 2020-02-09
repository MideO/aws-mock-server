output "public_ip" {
  value = "${aws_instance.mockserver.public_ip}"
}


output "mockserver_loadbalancer_dns_name" {
  value = "${data.aws_elb.lb.dns_name}"
}
