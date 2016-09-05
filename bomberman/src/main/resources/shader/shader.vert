#version 330 core

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix = mat4(1.0);

layout(location = 0) in vec3 in_position;
layout(location = 1) in vec2 in_texCoord;

out vec2 pass_texCoord;

void main () {
  gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4 (in_position, 1.0);
  pass_texCoord = in_texCoord;
}