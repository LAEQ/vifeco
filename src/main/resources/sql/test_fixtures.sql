INSERT INTO USER(FIRST_NAME, LAST_NAME, EMAIL, IS_DEFAULT) VALUES ('Luck', 'Skywalker', 'luke@maytheforcebewithyou.com', true);
INSERT INTO USER(FIRST_NAME, LAST_NAME, EMAIL) VALUES ('Darth', 'Vador', 'darth@iamyourfather.com');
INSERT INTO USER(FIRST_NAME, LAST_NAME, EMAIL) VALUES ('Leia', 'Organa', 'leia@areyoualittleshortforastormtrooper.com');
INSERT INTO USER(FIRST_NAME, LAST_NAME, EMAIL) VALUES ('Han', 'Solo', 'han@bestpilotinthegalaxy.com');

INSERT INTO CATEGORY (NAME, ICON, COLOR, SHORTCUT) VALUES ('Moving car', 'M11 11v-3h1.247c.882 0 1.235.297 1.828.909.452.465 1.925 2.091 1.925 2.091h-5zm-1-3h-2.243c-.688 0-1.051.222-1.377.581-.316.348-.895.948-1.506 1.671 1.719.644 4.055.748 5.126.748v-3zm14 5.161c0-2.823-2.03-3.41-2.794-3.631-1.142-.331-1.654-.475-3.031-.794-.55-.545-2.052-2.036-2.389-2.376l-.089-.091c-.666-.679-1.421-1.269-3.172-1.269h-7.64c-.547 0-.791.456-.254.944-.534.462-1.944 1.706-2.34 2.108-1.384 1.402-2.291 2.48-2.291 4.603 0 2.461 1.361 4.258 3.179 4.332.41 1.169 1.512 2.013 2.821 2.013 1.304 0 2.403-.838 2.816-2h6.367c.413 1.162 1.512 2 2.816 2 1.308 0 2.409-.843 2.82-2.01 1.934-.056 3.181-1.505 3.181-3.829zm-18 4.039c-.662 0-1.2-.538-1.2-1.2s.538-1.2 1.2-1.2 1.2.538 1.2 1.2-.538 1.2-1.2 1.2zm12 0c-.662 0-1.2-.538-1.2-1.2s.538-1.2 1.2-1.2 1.2.538 1.2 1.2-.538 1.2-1.2 1.2zm2.832-2.15c-.399-1.188-1.509-2.05-2.832-2.05-1.327 0-2.44.868-2.836 2.062h-6.328c-.396-1.194-1.509-2.062-2.836-2.062-1.319 0-2.426.857-2.829 2.04-.586-.114-1.171-1.037-1.171-2.385 0-1.335.47-1.938 1.714-3.199.725-.735 1.31-1.209 2.263-2.026.34-.291.774-.432 1.222-.43h5.173c1.22 0 1.577.385 2.116.892.419.393 2.682 2.665 2.682 2.665s2.303.554 3.48.895c.84.243 1.35.479 1.35 1.71 0 1.196-.396 1.826-1.168 1.888z', '#000000', 'A');
INSERT INTO CATEGORY (NAME, ICON, COLOR, SHORTCUT) VALUES ('Moving bike', 'M6.804 10.336l1.181-2.331-.462-1.005h-4.523v-1h5.992c.238 0 .5.19.5.5 0 .311-.26.5-.5.5h-.368l.47 1h6.483l-.841-2h3.243c.823.005 1.49.675 1.49 1.5 0 .828-.672 1.5-1.5 1.5-.711 0-.727-1 0-1 .239 0 .5-.189.5-.5 0-.239-.189-.5-.5-.5h-1.727l1.324 3.209c.454-.136.936-.209 1.434-.209 2.76 0 5 2.24 5 5s-2.24 5-5 5c-2.759 0-5-2.24-5-5 0-1.906 1.069-3.564 2.64-4.408l-.43-1.039-4.493 5.947h-1.742c-.251 2.525-2.384 4.5-4.975 4.5-2.759 0-5-2.24-5-5s2.241-5 5-5c.636 0 1.244.119 1.804.336zm-.455.897c-.421-.151-.876-.233-1.349-.233-2.207 0-4 1.792-4 4s1.793 4 4 4c2.038 0 3.723-1.528 3.97-3.5h-3.103c-.174.299-.497.5-.867.5-.551 0-1-.448-1-1 0-.533.419-.97.946-.998l1.403-2.769zm10.675.29c-1.208.688-2.024 1.988-2.024 3.477 0 2.208 1.792 4 4 4s4-1.792 4-4-1.792-4-4-4c-.363 0-.716.049-1.05.14l1.182 2.869c.49.064.868.484.868.991 0 .552-.448 1-1 1-.551 0-1-.448-1-1 0-.229.077-.44.207-.609l-1.183-2.868zm-9.783.164l-1.403 2.766.029.047h3.103c-.147-1.169-.798-2.183-1.729-2.813zm.454-.898c1.254.804 2.126 2.152 2.28 3.711h.998l-2.455-5.336-.823 1.625zm7.683-1.789h-5.839l2.211 4.797 3.628-4.797zm-14.378 0h4v-1h-4v1zm1-4h4v-1h-4v1z', '#00000000', 'S');
INSERT INTO CATEGORY (NAME, ICON, COLOR, SHORTCUT) VALUES ('Moving truck', 'M5 11v1h8v-7h-10v-1c0-.552.448-1 1-1h10c.552 0 1 .448 1 1v2h4.667c1.117 0 1.6.576 1.936 1.107.594.94 1.536 2.432 2.109 3.378.188.312.288.67.288 1.035v4.48c0 1.089-.743 2-2 2h-1c0 1.656-1.344 3-3 3s-3-1.344-3-3h-4c0 1.656-1.344 3-3 3s-3-1.344-3-3h-1c-.552 0-1-.448-1-1v-6h-2v-2h7v2h-3zm3 5.8c.662 0 1.2.538 1.2 1.2 0 .662-.538 1.2-1.2 1.2-.662 0-1.2-.538-1.2-1.2 0-.662.538-1.2 1.2-1.2zm10 0c.662 0 1.2.538 1.2 1.2 0 .662-.538 1.2-1.2 1.2-.662 0-1.2-.538-1.2-1.2 0-.662.538-1.2 1.2-1.2zm-3-2.8h-10v2h.765c.549-.614 1.347-1 2.235-1 .888 0 1.686.386 2.235 1h5.53c.549-.614 1.347-1 2.235-1 .888 0 1.686.386 2.235 1h1.765v-4.575l-1.711-2.929c-.179-.307-.508-.496-.863-.496h-4.426v6zm1-5v3h5l-1.427-2.496c-.178-.312-.509-.504-.868-.504h-2.705zm-16-3h8v2h-8v-2z', '#00000000', 'D');
INSERT INTO CATEGORY (NAME, ICON, COLOR, SHORTCUT) VALUES ('Stopped car', 'M7 13.5c0-.828-.672-1.5-1.5-1.5s-1.5.672-1.5 1.5.672 1.5 1.5 1.5 1.5-.672 1.5-1.5zm9 1c0-.276-.224-.5-.5-.5h-7c-.276 0-.5.224-.5.5s.224.5.5.5h7c.276 0 .5-.224.5-.5zm4-1c0-.828-.672-1.5-1.5-1.5s-1.5.672-1.5 1.5.672 1.5 1.5 1.5 1.5-.672 1.5-1.5zm-17.298-6.5h-2.202c-.276 0-.5.224-.5.5v.511c0 .793.926.989 1.616.989l1.086-2zm19.318 3.168c-.761-1.413-1.699-3.17-2.684-4.812-.786-1.312-1.37-1.938-2.751-2.187-1.395-.25-2.681-.347-4.585-.347s-3.19.097-4.585.347c-1.381.248-1.965.875-2.751 2.187-.981 1.637-1.913 3.382-2.684 4.812-.687 1.273-.98 2.412-.98 3.806 0 1.318.42 2.415 1 3.817v2.209c0 .552.448 1 1 1h1.5c.552 0 1-.448 1-1v-1h13v1c0 .552.448 1 1 1h1.5c.552 0 1-.448 1-1v-2.209c.58-1.403 1-2.499 1-3.817 0-1.394-.293-2.533-.98-3.806zm-15.641-3.784c.67-1.117.852-1.149 1.39-1.246 1.268-.227 2.455-.316 4.231-.316s2.963.088 4.231.316c.538.097.72.129 1.39 1.246.408.681.81 1.388 1.195 2.081-1.456.22-4.02.535-6.816.535-3.048 0-5.517-.336-6.805-.555.382-.686.779-1.386 1.184-2.061zm11.595 10.616h-11.948c-1.671 0-3.026-1.354-3.026-3.026 0-1.641.506-2.421 1.184-3.678 1.041.205 3.967.704 7.816.704 3.481 0 6.561-.455 7.834-.672.664 1.231 1.166 2.01 1.166 3.646 0 1.672-1.355 3.026-3.026 3.026zm5.526-10c.276 0 .5.224.5.5v.511c0 .793-.926.989-1.616.989l-1.086-2h2.202z', '#00000000' ,'F');
INSERT INTO CATEGORY (NAME, ICON, COLOR, SHORTCUT) VALUES ('Parked car', 'M 18.046875 4.746094 C 18.046875 5.496094 17.496094 5.761719 16.742188 5.761719 L 16.277344 5.761719 L 16.277344 3.765625 L 16.921875 3.765625 C 17.632812 3.765625 18.046875 4.023438 18.046875 4.746094 Z M 22 3.019531 L 22 8.898438 C 22 10.058594 21.058594 11 19.898438 11 L 14.015625 11 C 12.855469 11 11.917969 10.058594 11.917969 8.898438 L 11.917969 3.019531 C 11.917969 1.855469 12.855469 0.917969 14.019531 0.917969 L 19.898438 0.917969 C 21.058594 0.917969 22 1.855469 22 3.019531 Z M 19.480469 4.695312 C 19.480469 4 19.265625 3.476562 18.847656 3.125 C 18.421875 2.773438 17.804688 2.597656 16.996094 2.597656 L 14.859375 2.597656 L 14.859375 9.320312 L 16.277344 9.320312 L 16.277344 6.929688 L 16.886719 6.929688 C 17.714844 6.929688 18.355469 6.738281 18.804688 6.351562 C 19.253906 5.96875 19.480469 5.417969 19.480469 4.695312 Z M 15.582031 15.125 C 15.582031 15.882812 16.199219 16.5 16.957031 16.5 C 17.71875 16.5 18.332031 15.882812 18.332031 15.125 C 18.332031 14.363281 17.71875 13.75 16.957031 13.75 C 16.199219 13.75 15.582031 14.363281 15.582031 15.125 Z M 7.792969 15.582031 C 7.539062 15.582031 7.332031 15.789062 7.332031 16.039062 C 7.332031 16.292969 7.539062 16.5 7.792969 16.5 L 14.207031 16.5 C 14.460938 16.5 14.667969 16.292969 14.667969 16.039062 C 14.667969 15.789062 14.460938 15.582031 14.207031 15.582031 Z M 3.667969 15.125 C 3.667969 15.882812 4.28125 16.5 5.042969 16.5 C 5.800781 16.5 6.417969 15.882812 6.417969 15.125 C 6.417969 14.363281 5.800781 13.75 5.042969 13.75 C 4.28125 13.75 3.667969 14.363281 3.667969 15.125 Z M 2.476562 9.164062 L 0.457031 9.164062 C 0.207031 9.164062 0 9.371094 0 9.625 L 0 10.09375 C 0 10.820312 0.847656 11 1.480469 11 Z M 19.898438 12.832031 L 18.515625 12.832031 C 18.949219 13.648438 19.25 14.351562 19.25 15.558594 C 19.25 17.089844 18.007812 18.332031 16.476562 18.332031 L 5.523438 18.332031 C 3.992188 18.332031 2.75 17.089844 2.75 15.558594 C 2.75 14.054688 3.214844 13.339844 3.835938 12.1875 C 4.789062 12.375 7.472656 12.832031 11 12.832031 C 11.804688 12.832031 12.585938 12.804688 13.320312 12.761719 C 12.21875 12.5625 11.28125 11.90625 10.699219 10.996094 C 8.046875 10.972656 5.898438 10.683594 4.761719 10.492188 C 5.113281 9.863281 5.476562 9.21875 5.847656 8.601562 C 6.460938 7.578125 6.628906 7.546875 7.121094 7.457031 C 8.046875 7.292969 8.941406 7.210938 10.082031 7.183594 L 10.082031 5.347656 C 8.816406 5.375 7.835938 5.46875 6.796875 5.652344 C 5.53125 5.882812 4.996094 6.457031 4.273438 7.660156 C 3.375 9.160156 2.523438 10.757812 1.816406 12.070312 C 1.183594 13.238281 0.917969 14.28125 0.917969 15.558594 C 0.917969 16.765625 1.300781 17.769531 1.832031 19.054688 L 1.832031 21.082031 C 1.832031 21.589844 2.242188 22 2.75 22 L 4.125 22 C 4.632812 22 5.042969 21.589844 5.042969 21.082031 L 5.042969 20.164062 L 16.957031 20.164062 L 16.957031 21.082031 C 16.957031 21.589844 17.367188 22 17.875 22 L 19.25 22 C 19.757812 22 20.167969 21.589844 20.167969 21.082031 L 20.167969 19.058594 C 20.699219 17.769531 21.082031 16.765625 21.082031 15.558594 C 21.082031 14.542969 20.902344 13.671875 20.515625 12.769531 C 20.3125 12.804688 20.109375 12.832031 19.898438 12.832031 Z M 19.898438 12.832031 ', '#00000000' ,'1');
INSERT INTO CATEGORY (NAME, ICON, COLOR, SHORTCUT) VALUES ('Stopped truck', 'M3 18h-2c-.552 0-1-.448-1-1v-13c0-.552.448-1 1-1h13c.552 0 1 .448 1 1v2h4.667c1.117 0 1.6.576 1.936 1.107.594.94 1.536 2.432 2.109 3.378.188.312.288.67.288 1.035v4.48c0 1.089-.743 2-2 2h-1c0 1.656-1.344 3-3 3s-3-1.344-3-3h-6c0 1.656-1.344 3-3 3s-3-1.344-3-3zm3-1.2c.662 0 1.2.538 1.2 1.2 0 .662-.538 1.2-1.2 1.2-.662 0-1.2-.538-1.2-1.2 0-.662.538-1.2 1.2-1.2zm12 0c.662 0 1.2.538 1.2 1.2 0 .662-.538 1.2-1.2 1.2-.662 0-1.2-.538-1.2-1.2 0-.662.538-1.2 1.2-1.2zm-3-2.8h-13v2h1.765c.549-.614 1.347-1 2.235-1 .888 0 1.686.386 2.235 1h7.53c.549-.614 1.347-1 2.235-1 .888 0 1.686.386 2.235 1h1.765v-4.575l-1.711-2.929c-.179-.307-.508-.496-.863-.496h-4.426v6zm-2-9h-11v7h11v-7zm3 4v3h5l-1.427-2.496c-.178-.312-.509-.504-.868-.504h-2.705z',  '#00000000' ,'3');
INSERT INTO CATEGORY (NAME, ICON, COLOR, SHORTCUT) VALUES ('Charging Station', 'M 16.269531 9.167969 C 16.398438 9.167969 16.5 9.269531 16.5 9.394531 L 16.5 9.628906 C 16.5 9.992188 16.074219 10.082031 15.757812 10.082031 L 15.261719 9.167969 Z M 15.609375 10.773438 C 15.902344 11.316406 16.042969 11.804688 16.042969 12.453125 C 16.042969 13.082031 15.863281 13.617188 15.582031 14.277344 L 15.582031 15.125 C 15.582031 15.378906 15.378906 15.582031 15.125 15.582031 L 14.4375 15.582031 C 14.183594 15.582031 13.976562 15.378906 13.976562 15.125 L 13.976562 14.667969 L 8.019531 14.667969 L 8.019531 15.125 C 8.019531 15.378906 7.816406 15.582031 7.5625 15.582031 L 6.875 15.582031 C 6.621094 15.582031 6.417969 15.378906 6.417969 15.125 L 6.417969 14.277344 C 6.136719 13.613281 5.957031 13.082031 5.957031 12.453125 C 5.957031 11.804688 6.097656 11.316406 6.390625 10.773438 C 6.769531 10.070312 7.238281 9.191406 7.730469 8.367188 C 8.113281 7.734375 8.308594 7.582031 8.777344 7.5 C 9.480469 7.375 10.113281 7.332031 11 7.332031 C 11.886719 7.332031 12.519531 7.375 13.222656 7.5 C 13.691406 7.582031 13.886719 7.730469 14.269531 8.367188 C 14.761719 9.191406 15.230469 10.070312 15.609375 10.773438 Z M 8.25 12.144531 C 8.25 11.765625 7.941406 11.457031 7.5625 11.457031 C 7.183594 11.457031 6.875 11.765625 6.875 12.144531 C 6.875 12.523438 7.183594 12.832031 7.5625 12.832031 C 7.941406 12.832031 8.25 12.523438 8.25 12.144531 Z M 12.832031 12.605469 C 12.832031 12.476562 12.730469 12.375 12.605469 12.375 L 9.394531 12.375 C 9.269531 12.375 9.167969 12.476562 9.167969 12.605469 C 9.167969 12.730469 9.269531 12.832031 9.394531 12.832031 L 12.605469 12.832031 C 12.730469 12.832031 12.832031 12.730469 12.832031 12.605469 Z M 14.183594 10.070312 C 14.183594 10.070312 13.84375 9.234375 13.433594 8.628906 C 13.34375 8.492188 13.199219 8.398438 13.035156 8.371094 C 12.34375 8.246094 11.734375 8.207031 11 8.207031 C 10.265625 8.207031 9.65625 8.246094 8.964844 8.371094 C 8.804688 8.398438 8.660156 8.492188 8.566406 8.628906 C 8.160156 9.234375 7.820312 10.070312 7.820312 10.070312 C 8.570312 10.214844 9.792969 10.296875 11 10.296875 C 12.207031 10.296875 13.429688 10.214844 14.183594 10.070312 Z M 15.125 12.144531 C 15.125 11.765625 14.816406 11.457031 14.4375 11.457031 C 14.058594 11.457031 13.75 11.765625 13.75 12.144531 C 13.75 12.523438 14.058594 12.832031 14.4375 12.832031 C 14.816406 12.832031 15.125 12.523438 15.125 12.144531 Z M 6.738281 9.167969 L 5.730469 9.167969 C 5.601562 9.167969 5.5 9.269531 5.5 9.394531 L 5.5 9.628906 C 5.5 9.992188 5.925781 10.082031 6.242188 10.082031 Z M 21.550781 7.949219 L 19.832031 8.601562 C 19.953125 9.03125 20.050781 9.472656 20.101562 9.925781 L 21.9375 9.925781 C 21.871094 9.246094 21.738281 8.585938 21.550781 7.949219 Z M 20.085938 4.828125 L 18.707031 6.054688 C 18.960938 6.449219 19.175781 6.875 19.371094 7.308594 L 21.085938 6.65625 C 20.808594 6.011719 20.476562 5.398438 20.085938 4.828125 Z M 20.167969 11.300781 C 20.007812 16.214844 15.953125 20.167969 11 20.167969 C 5.945312 20.167969 1.832031 16.054688 1.832031 11 C 1.832031 9.65625 2.097656 7.609375 3.480469 7.476562 C 4.875 7.339844 5.5 7.03125 6.117188 6.253906 C 6.5625 5.691406 7.234375 4.855469 7.234375 4.855469 L 6.320312 4.125 L 7.691406 2.410156 C 7.890625 2.160156 7.851562 1.789062 7.597656 1.589844 C 7.347656 1.386719 6.980469 1.429688 6.777344 1.679688 L 5.40625 3.394531 L 4.492188 2.664062 L 5.863281 0.949219 C 6.066406 0.699219 6.023438 0.328125 5.769531 0.128906 C 5.519531 -0.0742188 5.148438 -0.03125 4.949219 0.21875 L 3.578125 1.933594 L 2.664062 1.203125 C 2.664062 1.203125 1.988281 2.007812 1.558594 2.578125 C 0.988281 3.34375 0.566406 4.394531 0.789062 5.363281 C 1.1875 7.105469 0 7.582031 0 11 C 0 17.074219 4.925781 22 11 22 C 16.972656 22 21.839844 17.234375 22 11.300781 Z M 17.863281 4.964844 L 19.234375 3.742188 C 18.816406 3.265625 18.367188 2.816406 17.867188 2.417969 L 16.679688 3.824219 C 17.109375 4.167969 17.5 4.554688 17.863281 4.964844 Z M 17.863281 4.964844 ',  '#00000000' ,'4');
INSERT INTO CATEGORY (NAME, ICON, COLOR, SHORTCUT) VALUES ('Electric car', 'M21.739 13.921c-1.347-.39-1.885-.538-3.552-.921 0 0-2.379-2.359-2.832-2.816-.568-.572-1.043-1.184-2.949-1.184h-7.894c-.511 0-.735.547-.069 1-.743.602-1.62 1.38-2.258 2.027-1.436 1.455-2.185 2.385-2.185 4.255 0 1.76 1.042 3.718 3.174 3.718h.01c.413 1.162 1.512 2 2.816 2 1.304 0 2.403-.838 2.816-2h6.367c.413 1.162 1.512 2 2.816 2s2.403-.838 2.816-2h.685c1.994 0 2.5-1.776 2.5-3.165 0-2.041-1.123-2.584-2.261-2.914zm-15.739 6.279c-.662 0-1.2-.538-1.2-1.2s.538-1.2 1.2-1.2 1.2.538 1.2 1.2-.538 1.2-1.2 1.2zm3.576-6.2c-1.071 0-3.5-.106-5.219-.75.578-.75.998-1.222 1.27-1.536.318-.368.873-.714 1.561-.714h2.388v3zm1-3h1.835c.882 0 1.428.493 2.022 1.105.452.466 1.732 1.895 1.732 1.895h-5.588v-3zm7.424 9.2c-.662 0-1.2-.538-1.2-1.2s.538-1.2 1.2-1.2 1.2.538 1.2 1.2-.538 1.2-1.2 1.2zm-3.625-14.7c0 .276-.224.5-.5.5h-1.875v1h-1.889c-.843 0-1.588-.417-2.041-1.057-.418-.59-1.099-.943-1.822-.943h-.019c-4.017 0-5.255 3.215-3.502 5.254l-.735.677c-2.341-2.679-.663-6.931 4.237-6.931h.019c.724 0 1.404-.352 1.822-.943.453-.64 1.198-1.057 2.041-1.057h1.889v1h1.875c.276 0 .5.224.5.5s-.224.5-.5.5h-1.875v1h1.875c.276 0 .5.224.5.5z',  '#00000000' ,'5');
INSERT INTO CATEGORY (NAME, ICON, COLOR, SHORTCUT) VALUES ('Construction site', 'M16.916 11.532l-1.131-4.144 1.294-3.21 3.603 1.159-.194 2.438-3.572 3.757zm5.929-1.734c.707 2.066-1.26 2.442-1.852 2.312-.866-.197-1.911-.464-2.618-.65l3.081-3.254.286-3.579-5.243-1.685-1.766 4.383 2.202 8.076c.082.299-.151.579-.442.579h-15.035c-.312 0-.458-.274-.458-.458v-4.065c0-.311.273-.458.458-.458h6.542v-3.561c0-.252.206-.458.459-.458h3.141s3.552-4.795 4.257-5.817c.13-.189.388-.206.59-.118 1.558.682 5.031 2.246 6.406 2.932.091.045.146.135.148.237-.031.23-.78 2.929-.922 3.45.111.3.437 1.181.766 2.134zm1.154-5.584c0-.483-.268-.917-.699-1.132-1.389-.694-4.886-2.267-6.453-2.953-.198-.087-.41-.129-.618-.129-.449 0-.89.192-1.161.548-.547.719-3.389 4.641-3.961 5.432h-2.648c-.805 0-1.459.654-1.459 1.458v2.561h-5.542c-.701 0-1.458.559-1.458 1.458v4.065c0 .701.558 1.458 1.458 1.458h15.035c.978 0 1.654-.93 1.407-1.842l-.814-2.983c.417.112 2.376.633 3.687.931 1.215.278 2.163-.438 2.354-.6.56-.475.869-1.139.869-1.869 0-.637-.146-1.038-.868-2.998.877-3.187.876-3.236.871-3.405zm-6.999 18.766h-14c-1.103 0-2-.897-2-2 0-1.095.892-1.993 2-2h14c1.103 0 2 .898 2 2 0 1.103-.897 2-2 2zm0-5h-14.02c-1.646.011-2.98 1.351-2.98 3 0 1.656 1.344 3 3 3h14c1.655 0 3-1.344 3-3 0-1.655-1.345-3-3-3zm-.09 3.487c-.274 0-.5-.224-.5-.5 0-.277.226-.5.5-.5.276 0 .5.223.5.5 0 .276-.224.5-.5.5zm0-2c-.827 0-1.5.672-1.5 1.5 0 .827.673 1.5 1.5 1.5s1.5-.673 1.5-1.5c0-.828-.673-1.5-1.5-1.5zm-4.536 2c-.275 0-.501-.224-.501-.5 0-.277.226-.5.501-.5.274 0 .5.223.5.5 0 .276-.226.5-.5.5zm0-2c-.827 0-1.501.672-1.501 1.5 0 .827.674 1.5 1.501 1.5.827 0 1.5-.673 1.5-1.5 0-.828-.673-1.5-1.5-1.5zm-4.801 2c-.275 0-.5-.224-.5-.5 0-.277.225-.5.5-.5s.5.223.5.5c0 .276-.225.5-.5.5zm0-2c-.827 0-1.5.672-1.5 1.5 0 .827.673 1.5 1.5 1.5s1.5-.673 1.5-1.5c0-.828-.673-1.5-1.5-1.5zm-4.47 2c-.274 0-.5-.224-.5-.5 0-.277.226-.5.5-.5.275 0 .5.223.5.5 0 .276-.225.5-.5.5zm0-2c-.827 0-1.5.672-1.5 1.5 0 .827.673 1.5 1.5 1.5.828 0 1.5-.673 1.5-1.5 0-.828-.672-1.5-1.5-1.5zm-1.107-7.5h4v1h-4v-1zm0 2h4v1h-4v-1zm8-3v-2h3.095l.522 2h-3.617zm-1-3v4h5.912c-.347-1.334-.696-2.667-1.045-4h-4.867z',  '#00000000' ,'6');
INSERT INTO CATEGORY (NAME, ICON, COLOR, SHORTCUT) VALUES ('Construction site 2', 'M 1.742188 5.640625 C 2.039062 5.058594 2.527344 4.082031 2.816406 3.5 C 2.929688 3.273438 3.148438 3.136719 3.402344 3.113281 C 4.597656 3.003906 7.113281 2.816406 8.152344 2.75 C 8.632812 2.71875 8.984375 2.789062 9.289062 3.097656 L 11.726562 5.546875 C 11.949219 5.765625 12.070312 6.066406 12.070312 6.375 L 12.066406 14.28125 L 14.644531 16.4375 L 17.023438 13.816406 C 17.539062 13.25 18.476562 13.4375 18.722656 14.167969 C 19.542969 16.632812 21 21 21 21 L 10.5 21 L 14.054688 17.085938 L 8.753906 12.648438 L 7.222656 11.367188 L 8.9375 14.480469 C 9.027344 14.644531 9.082031 14.828125 9.09375 15.011719 L 9.441406 20.300781 C 9.460938 20.613281 9.246094 21 8.75 21 C 8.429688 21 8.152344 20.777344 8.078125 20.464844 C 7.808594 19.308594 7.179688 16.617188 7.023438 15.933594 C 7 15.828125 6.945312 15.734375 6.875 15.65625 C 6.394531 15.136719 4.476562 13.125 4.476562 13.125 C 4.476562 13.125 2.023438 18.957031 1.363281 20.585938 C 1.261719 20.828125 1.035156 21 0.707031 21 C 0.304688 21 0 20.664062 0 20.296875 C 0 20.167969 1.527344 12.800781 2.03125 10.113281 C 2.09375 9.789062 2.242188 9.488281 2.460938 9.238281 L 3.40625 8.175781 L 0.339844 5.609375 C -0.101562 5.238281 0.460938 4.566406 0.902344 4.9375 Z M 7.742188 10.660156 L 9.316406 11.980469 L 11.023438 13.410156 L 10.09375 8.203125 Z M 2.523438 6.296875 L 3.988281 7.519531 L 6.429688 4.765625 L 4.039062 4.605469 Z M 12.722656 0 C 13.902344 0 14.859375 0.960938 14.859375 2.140625 C 14.859375 3.320312 13.902344 4.28125 12.722656 4.28125 C 11.539062 4.28125 10.582031 3.320312 10.582031 2.140625 C 10.582031 0.960938 11.539062 0 12.722656 0 Z M 12.722656 0',  '#00000000' ,'7');
INSERT INTO CATEGORY (NAME, ICON, COLOR, SHORTCUT) VALUES ('Car CO2', 'M11 16v-3h1.247c.882 0 1.235.297 1.828.909.452.465 1.925 2.091 1.925 2.091h-5zm-1-3h-2.243c-.688 0-1.051.222-1.377.581-.316.348-.895.948-1.506 1.671 1.719.644 4.055.748 5.126.748v-3zm14 5.161c0-2.823-2.03-3.41-2.794-3.631-1.142-.331-1.654-.475-3.031-.794-.549-.545-2.051-2.035-2.389-2.375l-.089-.091c-.666-.68-1.421-1.27-3.172-1.27h-7.64c-.547 0-.791.456-.254.944-.534.462-1.945 1.705-2.341 2.107-1.383 1.403-2.29 2.481-2.29 4.604 0 2.461 1.361 4.258 3.179 4.332.41 1.169 1.512 2.013 2.821 2.013 1.304 0 2.403-.838 2.816-2h6.367c.413 1.162 1.512 2 2.816 2 1.308 0 2.409-.843 2.82-2.01 1.934-.056 3.181-1.505 3.181-3.829zm-18 4.039c-.662 0-1.2-.538-1.2-1.2s.538-1.2 1.2-1.2 1.2.538 1.2 1.2-.538 1.2-1.2 1.2zm12 0c-.662 0-1.2-.538-1.2-1.2s.538-1.2 1.2-1.2 1.2.538 1.2 1.2-.538 1.2-1.2 1.2zm2.832-2.15c-.399-1.188-1.509-2.05-2.832-2.05-1.327 0-2.44.868-2.836 2.062h-6.328c-.396-1.194-1.509-2.062-2.836-2.062-1.319 0-2.426.857-2.829 2.04-.586-.114-1.171-1.037-1.171-2.385 0-1.335.47-1.938 1.714-3.199.725-.735 1.309-1.209 2.263-2.025.34-.291.774-.432 1.222-.43h5.173c1.22 0 1.577.385 2.116.892.419.393 2.682 2.665 2.682 2.665s2.303.554 3.48.895c.84.243 1.35.479 1.35 1.71 0 1.195-.396 1.825-1.168 1.887zm-11.05-15.345c0 .477-.215.833-.589.833-.371 0-.597-.338-.597-.819 0-.476.218-.833.593-.833.382.001.593.379.593.819zm2.572 2.68c-.315 2.084-3.109 1.998-3.758.615-.847 1.575-3.412 1.154-3.622-.664-1.126-.227-1.974-1.221-1.974-2.413s.848-2.186 1.974-2.413l-.005-.049c0-1.359 1.102-2.461 2.462-2.461.986 0 1.831.584 2.224 1.421 1.114-1.083 3.022-.421 3.135 1.085 1.15.207 2.025 1.208 2.025 2.418 0 1.358-1.102 2.461-2.461 2.461zm-5.818-2.669c0-.546.342-.811.782-.811.196 0 .353.044.466.091l.113-.44c-.099-.051-.317-.11-.604-.11-.742 0-1.339.466-1.339 1.302 0 .699.436 1.226 1.284 1.226.298 0 .527-.055.629-.106l-.083-.432c-.109.044-.295.08-.462.08-.495 0-.786-.309-.786-.8zm3.832-.029c0-.677-.411-1.241-1.164-1.241-.724 0-1.193.55-1.193 1.284 0 .698.426 1.248 1.153 1.248.716 0 1.204-.487 1.204-1.291zm1.494 1.394h-.625s.595-.435.595-.848c0-.301-.206-.521-.582-.521-.225 0-.419.077-.544.171l.11.278c.087-.066.212-.138.355-.138.192 0 .273.107.273.243-.006.243-.285.471-.758.899v.235h1.177v-.319zm-8.862 1.919c-.552 0-1 .448-1 1s.448 1 1 1 1-.448 1-1-.448-1-1-1zm-1.706 2.621c-.414 0-.75.336-.75.75s.336.75.75.75.75-.336.75-.75-.336-.75-.75-.75zm-.794 2.379c-.276 0-.5.224-.5.5s.224.5.5.5.5-.224.5-.5-.224-.5-.5-.5z',  '#00000000' ,'8');
INSERT INTO CATEGORY (NAME, ICON, COLOR, SHORTCUT) VALUES ('Stopped truck 2', 'M3 18h-2c-.552 0-1-.448-1-1v-2h15v-9h4.667c1.117 0 1.6.576 1.936 1.107.594.94 1.536 2.432 2.109 3.378.188.312.288.67.288 1.035v4.48c0 1.121-.728 2-2 2h-1c0 1.656-1.344 3-3 3s-3-1.344-3-3h-6c0 1.656-1.344 3-3 3s-3-1.344-3-3zm3-1.2c.662 0 1.2.538 1.2 1.2 0 .662-.538 1.2-1.2 1.2-.662 0-1.2-.538-1.2-1.2 0-.662.538-1.2 1.2-1.2zm12 0c.662 0 1.2.538 1.2 1.2 0 .662-.538 1.2-1.2 1.2-.662 0-1.2-.538-1.2-1.2 0-.662.538-1.2 1.2-1.2zm-4-2.8h-14v-10c0-.552.448-1 1-1h12c.552 0 1 .448 1 1v10zm3-6v3h4.715l-1.427-2.496c-.178-.312-.509-.504-.868-.504h-2.42z',  '#00000000' ,'9');
INSERT INTO CATEGORY (NAME, ICON, COLOR, SHORTCUT) VALUES ('Moving car 2', 'M21.739 10.921c-1.347-.39-1.885-.538-3.552-.921 0 0-2.379-2.359-2.832-2.816-.568-.572-1.043-1.184-2.949-1.184h-7.894c-.511 0-.736.547-.07 1-.742.602-1.619 1.38-2.258 2.027-1.435 1.455-2.184 2.385-2.184 4.255 0 1.76 1.042 3.718 3.174 3.718h.01c.413 1.162 1.512 2 2.816 2 1.304 0 2.403-.838 2.816-2h6.367c.413 1.162 1.512 2 2.816 2s2.403-.838 2.816-2h.685c1.994 0 2.5-1.776 2.5-3.165 0-2.041-1.123-2.584-2.261-2.914zm-15.739 6.279c-.662 0-1.2-.538-1.2-1.2s.538-1.2 1.2-1.2 1.2.538 1.2 1.2-.538 1.2-1.2 1.2zm3.576-6.2c-1.071 0-3.5-.106-5.219-.75.578-.75.998-1.222 1.27-1.536.318-.368.873-.714 1.561-.714h2.388v3zm1-3h1.835c.882 0 1.428.493 2.022 1.105.452.466 1.732 1.895 1.732 1.895h-5.588v-3zm7.424 9.2c-.662 0-1.2-.538-1.2-1.2s.538-1.2 1.2-1.2 1.2.538 1.2 1.2-.538 1.2-1.2 1.2z',  '#00000000' ,'0');
INSERT INTO CATEGORY (NAME, ICON, COLOR, SHORTCUT) VALUES ('Construction site 3', 'M23 23h-22v-1h1v-8h-2v-10h2v-3h5v3h10v-3h5v3h2v10h-2v8h1v1zm-18-9h-1v8h1v-8zm15 0h-1v8h1v-8zm-3 0h-10v8h10v-8zm5-8h-20v6h20v-6zm-17.656 5h-2l1.312-4h2l-1.312 4zm4 0h-2l1.312-4h2l-1.312 4zm4 0h-2l1.312-4h2l-1.312 4zm4 0h-2l1.312-4h2l-1.312 4zm4 0h-2l1.312-4h2l-1.312 4zm-15.344-8h-1v1h1v-1zm15 0h-1v1h1v-1z',  '#00000000' ,'G');


INSERT INTO COLLECTION (NAME, IS_DEFAULT) VALUES('Default', true);
INSERT INTO COLLECTION (NAME, IS_DEFAULT) VALUES('Collection 1', false);
INSERT INTO COLLECTION (NAME, IS_DEFAULT) VALUES('Collection 2', false);

INSERT INTO CATEGORY_COLLECTION(COLLECTION_ID, CATEGORY_ID) VALUES(1, 1);
INSERT INTO CATEGORY_COLLECTION(COLLECTION_ID, CATEGORY_ID) VALUES(1, 2);
INSERT INTO CATEGORY_COLLECTION(COLLECTION_ID, CATEGORY_ID) VALUES(1, 3);
INSERT INTO CATEGORY_COLLECTION(COLLECTION_ID, CATEGORY_ID) VALUES(1, 4);
INSERT INTO CATEGORY_COLLECTION(COLLECTION_ID, CATEGORY_ID) VALUES(2, 2);
INSERT INTO CATEGORY_COLLECTION(COLLECTION_ID, CATEGORY_ID) VALUES(2, 3);
INSERT INTO CATEGORY_COLLECTION(COLLECTION_ID, CATEGORY_ID) VALUES(3, 1);
INSERT INTO CATEGORY_COLLECTION(COLLECTION_ID, CATEGORY_ID) VALUES(3, 3);
INSERT INTO CATEGORY_COLLECTION(COLLECTION_ID, CATEGORY_ID) VALUES(3, 4);

INSERT INTO VIDEO (PATH, DURATION, USER_ID, COLLECTION_ID) VALUES ('path/to/VIDEO1.mp4', 12345.00, 1, 1);
INSERT INTO VIDEO (PATH, DURATION, USER_ID, COLLECTION_ID) VALUES ('path/to/VIDEO2.mp4', 12345.00, 1, 1);
INSERT INTO VIDEO (PATH, DURATION, USER_ID, COLLECTION_ID) VALUES ('path/to/VIDEO3.mp4', 12345.00, 1, 2);
INSERT INTO VIDEO (PATH, DURATION, USER_ID, COLLECTION_ID) VALUES ('path/to/VIDEO4.mp4', 12345.00, 1, 3);

INSERT INTO POINT (X, Y, VIDEO_ID, CATEGORY_ID, START) VALUES (11, 10, 1, 1, 5555);
INSERT INTO POINT (X, Y, VIDEO_ID, CATEGORY_ID, START) VALUES (12, 10, 1, 2, 1111);
INSERT INTO POINT (X, Y, VIDEO_ID, CATEGORY_ID, START) VALUES (13, 10, 1, 1, 3333);
INSERT INTO POINT (X, Y, VIDEO_ID, CATEGORY_ID, START) VALUES (14, 10, 1, 1, 2222);
INSERT INTO POINT (X, Y, VIDEO_ID, CATEGORY_ID, START) VALUES (15, 10, 1, 3, 4444);
INSERT INTO POINT (X, Y, VIDEO_ID, CATEGORY_ID, START) VALUES (16, 10, 2, 1, 1111);
INSERT INTO POINT (X, Y, VIDEO_ID, CATEGORY_ID, START) VALUES (17, 10, 2, 1, 2222);
INSERT INTO POINT (X, Y, VIDEO_ID, CATEGORY_ID, START) VALUES (18, 10, 1, 1, 3333);
INSERT INTO POINT (X, Y, VIDEO_ID, CATEGORY_ID, START) VALUES (19, 10, 1, 2, 1111);
INSERT INTO POINT (X, Y, VIDEO_ID, CATEGORY_ID, START) VALUES (20, 10, 1, 3, 2222);
