#version 150 core

in vec4 vertexColor;
in vec2 UV0;

out vec4 FragColor;

uniform sampler2D u_Sampler0;

void main() {
    FragColor = texture(u_Sampler0, UV0) * vertexColor;
}
