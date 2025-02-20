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
)

circles = circles[::-1]

platforms = data_frame['platform'].tolist()
colors = plt.cm.tab10.colors[:len(platforms)]
platforms_colors = dict(zip(platforms, colors))
annotation_text = [f"{platform} ({times_used})" for platform, times_used in zip(platforms, data_frame['times_used'])]


def rgb_to_str(rgb):
    return f'rgb({int(rgb[0] * 255)}, {int(rgb[1] * 255)}, {int(rgb[2] * 255)})'

fig = go.Figure()

fig.update_xaxes(
    range=[-1.05, 1.05], # making slightly wider axes than -1 to 1 so no edge of circles cut-off
    showticklabels=False,
    showgrid=False,
    zeroline=False
)

fig.update_yaxes(
    range=[-1.05, 1.05],
    showticklabels=False,
    showgrid=False,
    zeroline=False,
)

for circle, platform in zip(circles, platforms):
    x, y, r = circle

    fig.add_shape(
        type="circle",
        xref="x", yref="y",
        x0=x-r, y0=y-r, x1=x+r, y1=y+r,
        fillcolor=rgb_to_str(platforms_colors[platform]), 
    )
    if(r > 0.1):
        fig.add_annotation(
            x=x, y=y,
            text=platform,
            showarrow=False,
            font=dict(size=15, color="white"),
        )

fig.add_trace(go.Scatter(
    x=[circle.x for circle in circles],
    y=[circle.y for circle in circles],
    mode="markers",
    marker=dict(size=0, opacity=0),  
    hoverinfo="text",
    text=annotation_text
))
    
fig.update_layout(
    title="Favourite platforms",
    width=800, 
    height=800, 
    plot_bgcolor="white",
    hovermode="closest")
fig.show()
