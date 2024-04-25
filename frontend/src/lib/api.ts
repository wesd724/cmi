const toKR = (value: number) => {
    return value.toLocaleString('ko-KR', { maximumFractionDigits: 8 });
}

export { toKR }