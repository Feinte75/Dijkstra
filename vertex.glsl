#version 150 core

uniform mat4 inOrthoMatrix;

in vec4 in_Position;
in vec4 in_Color;

out vec4 pass_Color;

void main(void) {
    gl_Position = in_Position;
    gl_Position = inOrthoMatrix * in_Position;
    pass_Color = in_Color;
}