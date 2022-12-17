#version 150 core

in vec3 fe_Position;
in vec4 fe_Color;

out vec4 vertexColor;

uniform mat4 u_ModelMatrix;

void main() {
    gl_Position = u_ModelMatrix * vec4(fe_Position, 1.0);
    vertexColor = fe_Color;
}
