#version 150 core

in vec3 fe_Position;
in vec3 fe_Color;

out vec4 vertexColor;

uniform mat4 ModelMat;

void main() {
    gl_Position = ModelMat * vec4(fe_Position, 1.0);
    vertexColor = vec4(fe_Color, 1.0);
}
