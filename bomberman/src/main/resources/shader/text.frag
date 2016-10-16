#version 330 core

uniform sampler2D texture_font;
uniform vec4 font_color;

in vec2 pass_texCoord;

out vec4 out_color;

void main () {
  out_color = vec4(1, 1, 1, texture2D(texture_font, pass_texCoord).r) * font_color;
}