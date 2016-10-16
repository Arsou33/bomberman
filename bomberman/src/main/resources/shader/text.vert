#version 330 core

layout(location = 0) in vec4 in_coord;

out vec2 pass_texCoord;

void main () {
  gl_Position = vec4 (in_coord.xy, 0, 1.0);
  pass_texCoord = in_coord.zw;
}