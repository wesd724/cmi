const toKR = (value: number | undefined) => {
    return value?.toLocaleString('ko-KR', { maximumFractionDigits: 8 }) ?? 0;
}

export { toKR }