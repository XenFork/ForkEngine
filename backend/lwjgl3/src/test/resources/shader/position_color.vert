#version 150 core

in vec2 Position;
in vec3 Color;

out vec4 vertexColor;

uniform mat4 ModelMat;

void main() {
    gl_Position = ModelMat * vec4(Position, 0.0, 1.0);
    vertexColor = vec4(Color, 1.0);
}
