#version 400

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix =mat4(1.0);

in vec3 vp;

void main () {
  gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4 (vp, 1.0);
}