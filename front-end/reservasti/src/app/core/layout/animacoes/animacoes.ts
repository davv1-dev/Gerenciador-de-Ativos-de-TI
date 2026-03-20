import { trigger, transition, style, query, animate, group } from '@angular/animations';

export const fadeAnimation = trigger('fadeAnimation', [
  transition('* => *', [

    // 1. O seu setup original (mantivemos exatamente igual)
    query(':enter, :leave', [
      style({
        position: 'absolute',
        top: 0,
        left: 0,
        width: '100%',
        opacity: 0,
      })
    ], { optional: true }),

    // 2. A CORREÇÃO: Dizemos que a página nova COMEÇA no tamanho 0.98
    query(':enter', [
      style({ transform: 'scale(0.98)' })
    ], { optional: true }),

    // 3. A sua animação original, mas TERMINANDO no tamanho real (scale 1)
    query(':enter', [
      animate('400ms ease-out',
        style({
          opacity: 1,
          transform: 'scale(1)' // 👈 O segredo para não dar o solavanco!
        })
      )
    ], { optional: true }),

    // 4. O seu leave original (mantivemos igualzinho)
    query(':leave', [
      animate('400ms ease-in',
        style({
          opacity: 0,
          transform: 'scale(1.02)'
        })
      )
    ], { optional: true }),
  ])
]);
