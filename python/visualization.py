import os
import numpy as np
import pandas as pd
import matplotlib.pyplot as plt
import circlify
import plotly.graph_objects as go

favourite_platforms_path = os.path.join(os.getenv("SPOTIFY_OUTPUT_PATH"), "favourite_platforms")

data_frame = pd.concat([
    pd.read_csv(os.path.join(favourite_platforms_path, f))
    for f in os.listdir(favourite_platforms_path) if f.startswith("part-")
    ])


circles = circlify.circlify(
    data_frame['times_used'].tolist(),
    show_enclosure=False,
    target_enclosure=circlify.Circle(x=0, y=0, r=1)
)

circles = circles[::-1]

platforms = data_frame['platform'].tolist()
colors = plt.cm.tab10.colors[:len(platforms)]
platforms_colors = dict(zip(platforms, colors))

x_vals = [circle.x for circle in circles]
y_vals = [circle.y for circle in circles]
r_vals = [circle.r for circle in circles]
color_vals = [platforms_colors[platform] for platform in platforms]

def rgb_to_str(rgb):
    return f'rgb({int(rgb[0] * 255)}, {int(rgb[1] * 255)}, {int(rgb[2] * 255)})'

fig = go.Figure()

# Добавление кругов на график с использованием добавления shapes
for x, y, r, color in zip(x_vals, y_vals, r_vals, color_vals):
    fig.add_shape(
        type="circle",
        xref="x", yref="y",
        x0=x - r, y0=y - r, x1=x + r, y1=y + r,
        fillcolor=rgb_to_str(color),  # Цвет круга
        line_color="LightSeaGreen",
        line_width=2
    )

# Аннотирование платформ
for x, y, platform in zip(x_vals, y_vals, platforms):
    fig.add_annotation(
        x=x,
        y=y,
        text=platform,
        showarrow=False,
        font=dict(color="white", size=12),
        align="center"
    )

# Настройки графика
fig.update_layout(
    title="Favourite platforms",
    xaxis=dict(showgrid=False, zeroline=False),
    yaxis=dict(showgrid=False, zeroline=False),
    showlegend=False
)

fig.show()

# # # Создание графика с помощью Plotly
# # fig = go.Figure()

# # # Set axes properties
# # fig.update_xaxes(
# #     range=[-1.05, 1.05], # making slightly wider axes than -1 to 1 so no edge of circles cut-off
# #     showticklabels=False,
# #     showgrid=False,
# #     zeroline=False
# # )

# # fig.update_yaxes(
# #     range=[-1.05, 1.05],
# #     showticklabels=False,
# #     showgrid=False,
# #     zeroline=False,
# # )

# # # add parent circles
# # for circle in circles:
# #     x, y, r = circle
# #     fig.add_shape(type="circle",
# #         xref="x", yref="y",
# #         x0=x-r, y0=y-r, x1=x+r, y1=y+r,
# #         # fillcolor="PaleTurquoise", # fill color if needed
# #         line_color="LightSeaGreen",
# #         line_width=2,
# #     )


    
# # # Set figure size
# # fig.update_layout(width=800, height=800, plot_bgcolor="white")
# # fig.show()


# fig, ax = plt.subplots(figsize=(10, 10))

# ax.set_title("Favourite platforms")
# ax.axis('off')

# lim = max(
#     max(
#         abs(circle.x) + circle.r,
#         abs(circle.y) + circle.r
#     )
#     for circle in circles
# )
# plt.xlim(-lim, lim)
# plt.ylim(-lim, lim)

# labels=data_frame['platform'].tolist()
# platforms=data_frame['platform'].tolist()
# colors=plt.cm.tab10.colors[:len(platforms)]

# platforms_colors = dict(zip(platforms, colors))

# for circle, label in zip(circles, labels):
#     x, y, r = circle.x, circle.y, circle.r
#     color=platforms_colors[label]
#     ax.add_patch(plt.Circle((x, y), r,color=color, alpha=0.2, linewidth=2))
#     plt.annotate(
#         label,
#         (x, y),
#         va='center',
#         ha='center',
#         color='white',
#         fontweight='bold'
#     )

# plt.show()