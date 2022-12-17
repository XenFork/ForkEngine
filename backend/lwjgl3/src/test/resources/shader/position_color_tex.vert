#version 150 core

in vec3 fe_Position;
in vec3 fe_Color;
in vec2 fe_TexCoord0;

out vec4 vertexColor;
out vec2 UV0;

uniform mat4 u_ProjectionViewMatrix, u_ModelMatrix;

void main() {
    gl_Position = u_ProjectionViewMatrix * u_ModelMatrix * vec4(fe_Position, 1.0);
    vertexColor = vec4(fe_Color, 1.0);
    UV0 = fe_TexCoord0;
}
